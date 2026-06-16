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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    
    // Componentes de mensalidade
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
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        // Abas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Aba 1: Registrar Pagamento
        JPanel registroPanel = criarPainelRegistro();
        tabbedPane.addTab("Registrar Pagamento", registroPanel);
        
        // Aba 2: Histórico de Pagamentos
        JPanel historicoPanel = criarPainelHistorico();
        tabbedPane.addTab("Histórico de Pagamentos", historicoPanel);
        
        // Aba 3: Mensalidades
        JPanel mensalidadePanel = criarPainelMensalidade();
        tabbedPane.addTab("Mensalidades", mensalidadePanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // ==================== PAINEL REGISTRAR PAGAMENTO ====================
    
    private JPanel criarPainelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Buscar Aluno por ID
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
        
        // Nome do Aluno
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
        
        // Plano
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
        
        // Valor
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
        
        // Forma de Pagamento
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
        
        // Competência
        gbc.gridx = 3; gbc.gridy = 2;
        JLabel lblCompetencia = new JLabel("Competência (MM/AAAA):");
        lblCompetencia.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblCompetencia, gbc);
        gbc.gridx = 4;
        txtCompetencia = new JTextField();
        txtCompetencia.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy")));
        txtCompetencia.setPreferredSize(new Dimension(100, 30));
        panel.add(txtCompetencia, gbc);
        
        // Botão Registrar
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
    
    // ==================== PAINEL HISTÓRICO ====================
    
    private JPanel criarPainelHistorico() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        String[] colunas = {"ID", "Aluno", "Valor", "Data", "Forma", "Competência", "Status"};
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
        
        // Botão Editar Status
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
    
    // ==================== PAINEL MENSALIDADES ====================
    
    private JPanel criarPainelMensalidade() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COR_FUNDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Filtro
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        filtroPanel.setBackground(Color.WHITE);
        filtroPanel.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        filtroPanel.add(new JLabel("Mês:"));
        cbMesFiltro = new JComboBox<>(new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
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
        
        // Tabela
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
        
        // Cores na tabela
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
                    } else {
                        c.setBackground(COR_ATRASADO);
                        c.setForeground(Color.WHITE);
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
        
        // Total
        lblTotalMensalidade = new JLabel("Total arrecadado: R$ 0,00");
        lblTotalMensalidade.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalMensalidade.setForeground(COR_BOTAO_AZUL);
        lblTotalMensalidade.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(lblTotalMensalidade, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ==================== MÉTODOS ====================
    
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
                lblAlunoNome.setText("Aluno não encontrado!");
                lblAlunoNome.setForeground(Color.RED);
                return;
            }
            
            lblAlunoNome.setText(aluno.getNomeCompleto());
            lblAlunoNome.setForeground(new Color(46, 204, 113));
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido!");
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
                JOptionPane.showMessageDialog(this, "Busque e selecione um aluno válido!");
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
            LocalDate competencia = LocalDate.of(Integer.parseInt(partes[1]), Integer.parseInt(partes[0]), 1);
            
            Matricula matricula = matriculaController.buscarPorAluno(alunoSelecionado);
            if (matricula == null) {
                JOptionPane.showMessageDialog(this, "Aluno não possui matrícula ativa!");
                return;
            }
            
            Pagamento pagamento = new Pagamento(alunoSelecionado, matricula, plano.getValor(), forma, competencia);
            financeiroController.registrarPagamento(pagamento);
            
            JOptionPane.showMessageDialog(this, "Pagamento registrado com sucesso!");
            
            txtBuscaAluno.setText("");
            lblAlunoNome.setText("Nenhum aluno selecionado");
            lblAlunoNome.setForeground(Color.GRAY);
            
            carregarDados();
            carregarMensalidades();
            atualizarOutrasTelas();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar: " + e.getMessage());
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
                    case CARTAO_CREDITO: forma = "Cartão Crédito"; break;
                    case CARTAO_DEBITO: forma = "Cartão Débito"; break;
                    case TRANSFERENCIA: forma = "Transferência"; break;
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
        int selectedRow = tabelaPagamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um pagamento na tabela!");
            return;
        }
        
        int pagamentoId = (int) modeloTabela.getValueAt(selectedRow, 0);
        String statusAtual = (String) modeloTabela.getValueAt(selectedRow, 6);
        
        String[] opcoes = {"PAGO", "PENDENTE", "ATRASADO"};
        
        String novoStatus = (String) JOptionPane.showInputDialog(this,
            "Pagamento ID: " + pagamentoId + "\n" +
            "Aluno: " + modeloTabela.getValueAt(selectedRow, 1) + "\n" +
            "Valor: " + modeloTabela.getValueAt(selectedRow, 2) + "\n\n" +
            "Novo status:",
            "Editar Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            statusAtual);
        
        if (novoStatus != null && !novoStatus.equals(statusAtual)) {
            try {
                java.sql.Connection conn = dao.ConexaoBD.getConexao();
                java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE pagamentos SET status = ? WHERE id = ?");
                stmt.setString(1, novoStatus);
                stmt.setInt(2, pagamentoId);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
                
                modeloTabela.setValueAt(novoStatus, selectedRow, 6);
                
                JOptionPane.showMessageDialog(this, "Status atualizado para: " + novoStatus);
                
                carregarMensalidades();
                atualizarOutrasTelas();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
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
            
            java.util.Map<Integer, Pagamento> pagamentosMap = new HashMap<>();
            for (Pagamento p : pagamentos) {
                if (p.getAluno() != null) {
                    pagamentosMap.put(p.getAluno().getId(), p);
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
                    } else {
                        status = "PENDENTE";
                    }
                    valor = "R$ 0,00";
                }
                
                modeloTabelaMensalidade.addRow(new Object[]{a.getNomeCompleto(), valor, status});
            }
            
            lblTotalMensalidade.setText(String.format("Total arrecadado em %s/%d: R$ %.2f", 
                cbMesFiltro.getSelectedItem(), ano, total));
                
        } catch (Exception e) {
            e.printStackTrace();
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
}