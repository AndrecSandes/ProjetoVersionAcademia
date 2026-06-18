package view;

import controller.FinanceiroController;
import controller.AlunoController;
import controller.PlanoController;
import controller.MatriculaController;
import model.Pagamento;
import model.Pagamento.FormaPagamento;
import model.Aluno;
import model.Plano;
import model.Matricula;
import dao.ConexaoBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PainelPagamentos extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private FinanceiroController financeiroController;
    private AlunoController alunoController;
    private PlanoController planoController;
    private MatriculaController matriculaController;
    
    private JTable tabelaPagamentos;
    private DefaultTableModel modeloTabela;
    private JTextField txtBuscaAluno;
    private JLabel lblAlunoNome;
    private JComboBox<Plano> cbPlano;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JTextField txtCompetencia;
    
    private JComboBox<String> cbMesFiltro;
    private JComboBox<String> cbAnoFiltro;
    private JLabel lblTotalMensalidade;
    private JTable tabelaMensalidade;
    private DefaultTableModel modeloTabelaMensalidade;
    private JTabbedPane tabbedPane;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_BOTAO_VERDE = new Color(46, 204, 113);
    private final Color COR_BOTAO_AZUL = new Color(41, 128, 185);
    private final Color COR_BOTAO_AMARELO = new Color(241, 196, 15);
    private final Color COR_PAGO = new Color(46, 204, 113);
    private final Color COR_PENDENTE = new Color(241, 196, 15);
    private final Color COR_ATRASADO = new Color(231, 76, 60);
    
    public PainelPagamentos() {
        financeiroController = new FinanceiroController();
        alunoController = new AlunoController();
        planoController = new PlanoController();
        matriculaController = new MatriculaController();
        initComponents();
        carregarDados();
        carregarPlanos();
        carregarMensalidades();
        testarConexao();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JPanel registroPanel = criarPainelRegistro();
        tabbedPane.addTab("Registrar Pagamento", registroPanel);
        
        JPanel historicoPanel = criarPainelHistorico();
        tabbedPane.addTab("Historico de Pagamentos", historicoPanel);
        
        JPanel mensalidadePanel = criarPainelMensalidade();
        tabbedPane.addTab("Mensalidades", mensalidadePanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblBuscaAluno = new JLabel("ID do Aluno:");
        lblBuscaAluno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblBuscaAluno, gbc);
        gbc.gridx = 1;
        txtBuscaAluno = new JTextField(10);
        txtBuscaAluno.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(txtBuscaAluno, gbc);
        
        gbc.gridx = 2;
        JButton btnBuscarAluno = new JButton("Buscar");
        btnBuscarAluno.setBackground(COR_BOTAO_AZUL);
        btnBuscarAluno.setForeground(Color.WHITE);
        btnBuscarAluno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscarAluno.setFocusPainted(false);
        btnBuscarAluno.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscarAluno.addActionListener(e -> buscarAlunoPorId());
        panel.add(btnBuscarAluno, gbc);
        
        gbc.gridx = 3; gbc.gridy = 0;
        JLabel lblNome = new JLabel("Aluno:");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblNome, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 2;
        lblAlunoNome = new JLabel("Nenhum aluno selecionado");
        lblAlunoNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAlunoNome.setForeground(Color.GRAY);
        panel.add(lblAlunoNome, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPlano = new JLabel("Plano:");
        lblPlano.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblPlano, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        cbPlano = new JComboBox<>();
        cbPlano.setPreferredSize(new Dimension(200, 30));
        cbPlano.addActionListener(e -> atualizarValorPorPlano());
        cbPlano.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Plano) {
                    Plano plano = (Plano) value;
                    value = plano.getNome() + " - R$ " + String.format("%.2f", plano.getValor());
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        panel.add(cbPlano, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 3; gbc.gridy = 1;
        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblValor, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 2;
        JLabel lblValorExibido = new JLabel("0,00");
        lblValorExibido.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblValorExibido.setForeground(COR_BOTAO_VERDE);
        panel.add(lblValorExibido, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblForma = new JLabel("Forma Pagamento:");
        lblForma.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblForma, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        cbFormaPagamento = new JComboBox<>(FormaPagamento.values());
        cbFormaPagamento.setPreferredSize(new Dimension(200, 30));
        panel.add(cbFormaPagamento, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 3; gbc.gridy = 2;
        JLabel lblCompetencia = new JLabel("Competencia (MM/AAAA):");
        lblCompetencia.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblCompetencia, gbc);
        gbc.gridx = 4;
        txtCompetencia = new JTextField();
        txtCompetencia.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy")));
        txtCompetencia.setPreferredSize(new Dimension(100, 30));
        panel.add(txtCompetencia, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 4;
        JButton btnRegistrar = new JButton("Registrar Pagamento");
        btnRegistrar.setBackground(COR_BOTAO_VERDE);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(e -> registrarPagamento());
        panel.add(btnRegistrar, gbc);
        
        return panel;
    }
    
    private JPanel criarPainelHistorico() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        String[] colunas = {"ID", "Aluno", "Valor", "Data", "Forma", "Competencia", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaPagamentos = new JTable(modeloTabela);
        tabelaPagamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaPagamentos.getTableHeader().setBackground(new Color(44, 62, 80));
        tabelaPagamentos.getTableHeader().setForeground(Color.WHITE);
        tabelaPagamentos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaPagamentos.setRowHeight(35);
        tabelaPagamentos.setSelectionBackground(COR_BOTAO_AZUL);
        tabelaPagamentos.setSelectionForeground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(tabelaPagamentos);
        panel.add(scroll, BorderLayout.CENTER);
        
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(COR_PAINEL_BRANCO);
        
        JButton btnEditarStatus = new JButton("Editar Status do Pagamento");
        btnEditarStatus.setBackground(COR_BOTAO_AMARELO);
        btnEditarStatus.setForeground(Color.WHITE);
        btnEditarStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditarStatus.setFocusPainted(false);
        btnEditarStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditarStatus.addActionListener(e -> editarStatusPagamento());
        botoesPanel.add(btnEditarStatus);
        
        panel.add(botoesPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarPainelMensalidade() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COR_FUNDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        filtroPanel.setBackground(Color.WHITE);
        filtroPanel.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        filtroPanel.add(new JLabel("Mes:"));
        cbMesFiltro = new JComboBox<>(new String[]{"Janeiro", "Fevereiro", "Marco", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"});
        cbMesFiltro.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        filtroPanel.add(cbMesFiltro);
        
        filtroPanel.add(new JLabel("Ano:"));
        cbAnoFiltro = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual - 1; i <= anoAtual + 1; i++) {
            cbAnoFiltro.addItem(String.valueOf(i));
        }
        cbAnoFiltro.setSelectedItem(String.valueOf(anoAtual));
        filtroPanel.add(cbAnoFiltro);
        
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(COR_BOTAO_AZUL);
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFiltrar.addActionListener(e -> carregarMensalidades());
        filtroPanel.add(btnFiltrar);
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        
        String[] colunasMensalidade = {"Aluno", "Valor", "Status"};
        modeloTabelaMensalidade = new DefaultTableModel(colunasMensalidade, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaMensalidade = new JTable(modeloTabelaMensalidade);
        tabelaMensalidade.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaMensalidade.getTableHeader().setBackground(new Color(44, 62, 80));
        tabelaMensalidade.getTableHeader().setForeground(Color.WHITE);
        tabelaMensalidade.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaMensalidade.setRowHeight(35);
        
        tabelaMensalidade.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && column == 2) {
                    String status = (String) table.getValueAt(row, 2);
                    if ("PAGO".equals(status)) {
                        c.setBackground(COR_PAGO);
                        c.setForeground(Color.WHITE);
                    } else if ("PENDENTE".equals(status)) {
                        c.setBackground(COR_PENDENTE);
                        c.setForeground(Color.BLACK);
                    } else if ("ATRASADO".equals(status)) {
                        c.setBackground(COR_ATRASADO);
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } else if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
        
        JScrollPane scroll = new JScrollPane(tabelaMensalidade);
        panel.add(scroll, BorderLayout.CENTER);
        
        lblTotalMensalidade = new JLabel("Total arrecadado: R$ 0,00");
        lblTotalMensalidade.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalMensalidade.setForeground(COR_BOTAO_AZUL);
        lblTotalMensalidade.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(lblTotalMensalidade, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void carregarPlanos() {
        try {
            cbPlano.removeAllItems();
            List<Plano> planos = planoController.listarTodos();
            for (Plano p : planos) {
                cbPlano.addItem(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void buscarAlunoPorId() {
        try {
            String idStr = txtBuscaAluno.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite o ID do aluno!");
                return;
            }
            
            int id = Integer.parseInt(idStr);
            Aluno aluno = alunoController.buscarPorId(id);
            
            if (aluno == null) {
                lblAlunoNome.setText("Aluno nao encontrado!");
                lblAlunoNome.setForeground(Color.RED);
                return;
            }
            
            lblAlunoNome.setText(aluno.getNomeCompleto());
            lblAlunoNome.setForeground(new Color(46, 204, 113));
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID invalido!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    private void atualizarValorPorPlano() {
        Plano planoSelecionado = (Plano) cbPlano.getSelectedItem();
        if (planoSelecionado != null) {
            Component[] components = ((JPanel) tabbedPane.getComponentAt(0)).getComponents();
            for (Component c : components) {
                if (c instanceof JLabel && ((JLabel) c).getFont().getSize() == 14) {
                    ((JLabel) c).setText(String.format("%.2f", planoSelecionado.getValor()));
                    break;
                }
            }
        }
    }
    
    private Aluno getAlunoSelecionado() {
        try {
            int id = Integer.parseInt(txtBuscaAluno.getText().trim());
            return alunoController.buscarPorId(id);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void registrarPagamento() {
        try {
            Aluno alunoSelecionado = getAlunoSelecionado();
            if (alunoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Busque e selecione um aluno valido!");
                return;
            }
            
            Plano plano = (Plano) cbPlano.getSelectedItem();
            if (plano == null) {
                JOptionPane.showMessageDialog(this, "Selecione um plano!");
                return;
            }
            
            FormaPagamento forma = (FormaPagamento) cbFormaPagamento.getSelectedItem();
            
            String competenciaStr = txtCompetencia.getText().trim();
            String[] partes = competenciaStr.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);
            LocalDate competencia = LocalDate.of(ano, mes, 1);
            
            boolean jaPago = financeiroController.verificarPagamentoMes(alunoSelecionado.getId(), mes, ano);
            if (jaPago) {
                JOptionPane.showMessageDialog(this, 
                    "Este aluno ja possui pagamento registrado para " + competenciaStr + "!\n" +
                    "Nao e permitido registrar dois pagamentos no mesmo mes.",
                    "Pagamento duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Matricula matricula = matriculaController.buscarPorAluno(alunoSelecionado);
            if (matricula == null) {
                JOptionPane.showMessageDialog(this, "Aluno nao possui matricula ativa!");
                return;
            }
            
            Pagamento pagamento = new Pagamento(alunoSelecionado, matricula, plano.getValor(), forma, competencia);
            pagamento.setStatus(Pagamento.StatusPagamento.PAGO);
            financeiroController.registrarPagamento(pagamento);
            
            JOptionPane.showMessageDialog(this, 
                "Pagamento registrado com sucesso!\n" +
                "Mensalidade de " + competenciaStr + " marcada como PAGA.",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            txtBuscaAluno.setText("");
            lblAlunoNome.setText("Nenhum aluno selecionado");
            lblAlunoNome.setForeground(Color.GRAY);
            
            carregarDados();
            carregarMensalidades();
            atualizarOutrasTelas();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarDados() {
        try {
            modeloTabela.setRowCount(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            List<Pagamento> pagamentos = financeiroController.getPagamentosPorPeriodo(
                LocalDate.of(2024, 1, 1), LocalDate.now());
            
            for (Pagamento p : pagamentos) {
                String forma = "";
                switch (p.getFormaPagamento()) {
                    case PIX: forma = "PIX"; break;
                    case DINHEIRO: forma = "Dinheiro"; break;
                    case CARTAO_CREDITO: forma = "Cartao Credito"; break;
                    case CARTAO_DEBITO: forma = "Cartao Debito"; break;
                    case TRANSFERENCIA: forma = "Transferencia"; break;
                }
                
                modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getAluno() != null ? p.getAluno().getNomeCompleto() : "N/A",
                    String.format("R$ %.2f", p.getValor()),
                    p.getDataPagamento().format(formatter),
                    forma,
                    p.getCompetencia().format(DateTimeFormatter.ofPattern("MM/yyyy")),
                    p.getStatus()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void editarStatusPagamento() {
        // Verifica se tem alguma linha selecionada
        int selectedRow = tabelaPagamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um pagamento na tabela para editar o status!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Pega os dados da linha selecionada
            int pagamentoId = (int) modeloTabela.getValueAt(selectedRow, 0);
            Object statusObj = modeloTabela.getValueAt(selectedRow, 6);
            String statusAtual = statusObj != null ? statusObj.toString() : "PENDENTE";
            String nomeAluno = (String) modeloTabela.getValueAt(selectedRow, 1);
            String valorPagamento = (String) modeloTabela.getValueAt(selectedRow, 2);
            
            System.out.println("Pagamento ID selecionado: " + pagamentoId);
            System.out.println("Status atual: " + statusAtual);
            
            String[] opcoes = {"PAGO", "PENDENTE", "ATRASADO"};
            
            // Dialog para escolher o novo status
            String novoStatus = (String) JOptionPane.showInputDialog(this,
                "Dados do Pagamento:\n" +
                "ID: " + pagamentoId + "\n" +
                "Aluno: " + nomeAluno + "\n" +
                "Valor: " + valorPagamento + "\n\n" +
                "Status atual: " + statusAtual + "\n\n" +
                "Selecione o novo status:",
                "Editar Status do Pagamento",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                statusAtual);
            
            System.out.println("Novo status selecionado: " + novoStatus);
            
            if (novoStatus != null && !novoStatus.equals(statusAtual)) {
                // Atualiza no banco de dados usando Connection direta
                Connection conn = null;
                PreparedStatement stmt = null;
                
                try {
                    conn = ConexaoBD.getConexao();
                    String sql = "UPDATE pagamentos SET status = ? WHERE id = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, novoStatus);
                    stmt.setInt(2, pagamentoId);
                    
                    int rowsAffected = stmt.executeUpdate();
                    System.out.println("Linhas afetadas: " + rowsAffected);
                    
                    if (rowsAffected > 0) {
                        // Atualiza a tabela visualmente
                        modeloTabela.setValueAt(novoStatus, selectedRow, 6);
                        
                        JOptionPane.showMessageDialog(this, 
                            "Status do pagamento atualizado para: " + novoStatus,
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Atualiza as outras telas
                        carregarMensalidades();
                        atualizarOutrasTelas();
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Nenhum registro foi atualizado. Verifique o ID do pagamento.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch (SQLException e) {
                    System.err.println("Erro SQL: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao atualizar status (SQL): " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro geral: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao processar edicao: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarMensalidades() {
        try {
            modeloTabelaMensalidade.setRowCount(0);
            
            int mes = cbMesFiltro.getSelectedIndex() + 1;
            int ano = Integer.parseInt((String) cbAnoFiltro.getSelectedItem());
            
            LocalDate inicio = LocalDate.of(ano, mes, 1);
            LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());
            
            List<Pagamento> pagamentos = financeiroController.getPagamentosPorPeriodo(inicio, fim);
            List<Aluno> alunos = alunoController.listarTodos();
            
            alunos.sort((a1, a2) -> Integer.compare(a1.getId(), a2.getId()));
            
            Map<Integer, Pagamento> pagamentosMap = new HashMap<>();
            for (Pagamento p : pagamentos) {
                if (p.getAluno() != null) {
                    if (p.getCompetencia().getMonthValue() == mes && 
                        p.getCompetencia().getYear() == ano) {
                        pagamentosMap.put(p.getAluno().getId(), p);
                    }
                }
            }
            
            double total = 0;
            
            for (Aluno a : alunos) {
                Pagamento p = pagamentosMap.get(a.getId());
                String status;
                String valor;
                
                if (p != null) {
                    status = "PAGO";
                    valor = String.format("R$ %.2f", p.getValor());
                    total += p.getValor();
                } else {
                    if (a.getStatus() == Aluno.StatusAluno.ATIVO) {
                        status = "ATRASADO";
                    } else if (a.getStatus() == Aluno.StatusAluno.PENDENTE) {
                        status = "PENDENTE";
                    } else {
                        status = "INATIVO";
                    }
                    valor = "R$ 0,00";
                }
                
                modeloTabelaMensalidade.addRow(new Object[]{a.getNomeCompleto(), valor, status});
            }
            
            lblTotalMensalidade.setText(String.format("Total arrecadado em %s/%d: R$ %.2f", 
                cbMesFiltro.getSelectedItem(), ano, total));
                
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar mensalidades: " + e.getMessage());
        }
    }
    
    private void atualizarOutrasTelas() {
        try {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            for (Component comp : parent.getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component c : ((JPanel) comp).getComponents()) {
                        if (c instanceof PainelDashboard) {
                            ((PainelDashboard) c).atualizar();
                        }
                        if (c instanceof PainelFinanceiro) {
                            ((PainelFinanceiro) c).atualizar();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        }
    }
    
    private void testarConexao() {
        try {
            Connection conn = ConexaoBD.getConexao();
            System.out.println("Conexao com banco OK!");
            conn.close();
        } catch (Exception e) {
            System.err.println("Erro de conexao: " + e.getMessage());
            e.printStackTrace();
        }
    }
}