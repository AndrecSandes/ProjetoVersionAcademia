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
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_BOTAO_VERDE = new Color(46, 204, 113);
    private final Color COR_BOTAO_AZUL = new Color(41, 128, 185);
    private final Color COR_BOTAO_AMARELO = new Color(241, 196, 15);
    
    public PainelPagamentos() {
        financeiroController = new FinanceiroController();
        alunoController = new AlunoController();
        planoController = new PlanoController();
        matriculaController = new MatriculaController();
        initComponents();
        carregarDados();
        carregarPlanos();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        JPanel formPanel = criarFormulario();
        add(formPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = criarPainelTabela();
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel botoesPanel = criarBotoesAcao();
        add(botoesPanel, BorderLayout.SOUTH);
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
    
    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
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
        btnBuscarAluno.setBorderPainted(false);
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
        
        // Plano - COM RENDERER PARA MOSTRAR O NOME CORRETAMENTE
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPlano = new JLabel("Plano:");
        lblPlano.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblPlano, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        cbPlano = new JComboBox<>();
        cbPlano.setPreferredSize(new Dimension(200, 30));
        cbPlano.addActionListener(e -> atualizarValorPorPlano());
        
        // Renderer para mostrar o nome do plano em vez do endereço de memória
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
        
        // Valor (R$) - APENAS ESTE, SEM O OUTRO
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
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(e -> registrarPagamento());
        panel.add(btnRegistrar, gbc);
        gbc.gridwidth = 1;
        
        return panel;
    }
    
    private void buscarAlunoPorId() {
        try {
            String idStr = txtBuscaAluno.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite o ID do aluno!", "Aviso", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarValorPorPlano() {
        Plano planoSelecionado = (Plano) cbPlano.getSelectedItem();
        if (planoSelecionado != null) {
            // Encontrar o JLabel do valor exibido
            Component[] components = ((JPanel) getComponent(0)).getComponents();
            for (Component c : components) {
                if (c instanceof JLabel && ((JLabel) c).getFont().getSize() == 14 && ((JLabel) c).getForeground() == COR_BOTAO_VERDE) {
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
                JOptionPane.showMessageDialog(this, "Busque e selecione um aluno válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Plano plano = (Plano) cbPlano.getSelectedItem();
            if (plano == null) {
                JOptionPane.showMessageDialog(this, "Selecione um plano!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            FormaPagamento forma = (FormaPagamento) cbFormaPagamento.getSelectedItem();
            
            String competenciaStr = txtCompetencia.getText().trim();
            String[] partes = competenciaStr.split("/");
            LocalDate competencia = LocalDate.of(Integer.parseInt(partes[1]), Integer.parseInt(partes[0]), 1);
            
            Matricula matricula = matriculaController.buscarPorAluno(alunoSelecionado);
            if (matricula == null) {
                JOptionPane.showMessageDialog(this, "Aluno não possui matrícula ativa!\n\nClique em 'Ver Matrícula' para criar uma.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Pagamento pagamento = new Pagamento(alunoSelecionado, matricula, plano.getValor(), forma, competencia);
            financeiroController.registrarPagamento(pagamento);
            
            JOptionPane.showMessageDialog(this, 
                "Pagamento registrado com sucesso!\n\n" +
                "Aluno: " + alunoSelecionado.getNomeCompleto() + "\n" +
                "Plano: " + plano.getNome() + "\n" +
                "Valor: R$ " + String.format("%.2f", plano.getValor()) + "\n" +
                "Forma: " + forma,
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            txtBuscaAluno.setText("");
            lblAlunoNome.setText("Nenhum aluno selecionado");
            lblAlunoNome.setForeground(Color.GRAY);
            carregarDados();
            
            // ATUALIZAR OUTRAS TELAS APÓS REGISTRAR O PAGAMENTO
            atualizarOutrasTelas();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void atualizarOutrasTelas() {
        try {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            
            // Percorre todos os componentes da janela principal
            for (Component comp : parent.getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel panel = (JPanel) comp;
                    
                    // Procura pelos painéis que precisam ser atualizados
                    for (Component c : panel.getComponents()) {
                        if (c instanceof PainelDashboard) {
                            ((PainelDashboard) c).atualizar();
                            System.out.println("Dashboard atualizado");
                        }
                        if (c instanceof PainelFinanceiro) {
                            ((PainelFinanceiro) c).atualizar();
                            System.out.println("Financeiro atualizado");
                        }
                        if (c instanceof PainelMensalidades) {
                            ((PainelMensalidades) c).atualizar();
                            System.out.println("Mensalidades atualizado");
                        }
                        if (c instanceof PainelAlunos) {
                            ((PainelAlunos) c).carregarDados();
                            System.out.println("Alunos atualizado");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar outras telas: " + e.getMessage());
        }
    }
    
    private JPanel criarPainelTabela() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        String[] colunas = {"ID", "Aluno", "Valor", "Data Pagamento", "Forma Pagamento", "Competência", "Status"};
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
        tabelaPagamentos.setRowHeight(30);
        tabelaPagamentos.setSelectionBackground(COR_BOTAO_AZUL);
        tabelaPagamentos.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabelaPagamentos);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarBotoesAcao() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        JButton btnEditarStatus = new JButton("Editar Status do Pagamento");
        btnEditarStatus.setBackground(COR_BOTAO_AMARELO);
        btnEditarStatus.setForeground(Color.WHITE);
        btnEditarStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditarStatus.setFocusPainted(false);
        btnEditarStatus.setBorderPainted(false);
        btnEditarStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditarStatus.addActionListener(e -> editarStatusPagamento());
        
        panel.add(btnEditarStatus);
        
        return panel;
    }
    
    private void editarStatusPagamento() {
        int selectedRow = tabelaPagamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um pagamento para editar o status",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String[] opcoes = {"PAGO", "PENDENTE", "ATRASADO"};
        String statusAtual = (String) modeloTabela.getValueAt(selectedRow, 6);
        
        String novoStatus = (String) JOptionPane.showInputDialog(this,
            "Selecione o novo status do pagamento:\n\n" +
            "Pagamento ID: " + modeloTabela.getValueAt(selectedRow, 0) + "\n" +
            "Aluno: " + modeloTabela.getValueAt(selectedRow, 1) + "\n" +
            "Valor: " + modeloTabela.getValueAt(selectedRow, 2),
            "Editar Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            statusAtual);
        
        if (novoStatus != null && !novoStatus.equals(statusAtual)) {
            try {
                int pagamentoId = (int) modeloTabela.getValueAt(selectedRow, 0);
                // Aqui você implementaria a atualização no banco
                // Por enquanto, apenas atualiza a tabela visualmente
                modeloTabela.setValueAt(novoStatus, selectedRow, 6);
                
                JOptionPane.showMessageDialog(this,
                    "Status do pagamento atualizado para: " + novoStatus,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar status: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
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
}