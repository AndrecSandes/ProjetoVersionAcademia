package view;

import controller.AlunoController;
import controller.MatriculaController;
import controller.FinanceiroController;
import model.Aluno;
import model.Aluno.StatusAluno;
import model.Matricula;
import model.Pagamento;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class PainelAlunos extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;
    private JTextField txtBusca;
    private JComboBox<String> cbTipoBusca;
    private JComboBox<String> cbFiltroStatus;
    private AlunoController alunoController;
    private MatriculaController matriculaController;
    private FinanceiroController financeiroController;
    private PainelDashboard dashboard;
    
    // Cores
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_TEXTO_ESCURO = new Color(51, 51, 51);
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_BOTAO_AZUL = new Color(41, 128, 185);
    private final Color COR_BOTAO_VERDE = new Color(46, 204, 113);
    private final Color COR_BOTAO_VERMELHO = new Color(231, 76, 60);
    private final Color COR_BOTAO_AMARELO = new Color(241, 196, 15);
    
    public PainelAlunos(PainelDashboard dashboard) {
        this.dashboard = dashboard;
        alunoController = new AlunoController();
        matriculaController = new MatriculaController();
        financeiroController = new FinanceiroController();
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        JPanel toolbar = criarToolbar();
        add(toolbar, BorderLayout.NORTH);
        
        JPanel tablePanel = criarPainelTabela();
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel botoesAcao = criarBotoesAcao();
        add(botoesAcao, BorderLayout.SOUTH);
    }
    
    private JPanel criarToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(15, 0));
        toolbar.setBackground(COR_PAINEL_BRANCO);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel pesquisaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pesquisaPanel.setBackground(COR_PAINEL_BRANCO);
        
        JLabel lblBusca = new JLabel("Buscar por:");
        lblBusca.setForeground(COR_TEXTO_ESCURO);
        lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // ComboBox para escolher o tipo de busca
        cbTipoBusca = new JComboBox<>(new String[]{"Nome", "ID"});
        cbTipoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbTipoBusca.setBackground(Color.WHITE);
        cbTipoBusca.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        txtBusca = new JTextField(20);
        txtBusca.setToolTipText("Digite o nome ou ID do aluno");
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBusca.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        JButton btnPesquisar = criarBotao("Pesquisar", COR_BOTAO_AZUL);
        btnPesquisar.addActionListener(e -> pesquisarAlunos());
        
        // Botão para ver todos os alunos
        JButton btnVerTodos = criarBotao("Ver Todos", COR_BOTAO_VERDE);
        btnVerTodos.addActionListener(e -> carregarDados());
        
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setForeground(COR_TEXTO_ESCURO);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        cbFiltroStatus = new JComboBox<>(new String[]{"Todos", "ATIVO", "INATIVO", "PENDENTE"});
        cbFiltroStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbFiltroStatus.setBackground(Color.WHITE);
        cbFiltroStatus.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        cbFiltroStatus.addActionListener(e -> filtrarPorStatus());
        
        pesquisaPanel.add(lblBusca);
        pesquisaPanel.add(cbTipoBusca);
        pesquisaPanel.add(txtBusca);
        pesquisaPanel.add(btnPesquisar);
        pesquisaPanel.add(btnVerTodos);
        pesquisaPanel.add(Box.createHorizontalStrut(20));
        pesquisaPanel.add(lblStatus);
        pesquisaPanel.add(cbFiltroStatus);
        
        JButton btnNovo = criarBotao("+ Novo Aluno", COR_BOTAO_VERDE);
        btnNovo.addActionListener(e -> abrirFormularioAluno(null));
        
        toolbar.add(pesquisaPanel, BorderLayout.WEST);
        toolbar.add(btnNovo, BorderLayout.EAST);
        
        return toolbar;
    }
    
    private JPanel criarPainelTabela() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Email", "Data Matrícula", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaAlunos = new JTable(modeloTabela);
        
        tabelaAlunos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaAlunos.getTableHeader().setBackground(new Color(44, 62, 80));
        tabelaAlunos.getTableHeader().setForeground(Color.WHITE);
        tabelaAlunos.getTableHeader().setReorderingAllowed(false);
        
        tabelaAlunos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaAlunos.setRowHeight(35);
        tabelaAlunos.setForeground(COR_TEXTO_ESCURO);
        tabelaAlunos.setBackground(Color.WHITE);
        tabelaAlunos.setSelectionBackground(COR_BOTAO_AZUL);
        tabelaAlunos.setSelectionForeground(Color.WHITE);
        tabelaAlunos.setGridColor(COR_BORDA);
        tabelaAlunos.setShowVerticalLines(true);
        
        JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarBotoesAcao() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        JButton btnEditar = criarBotao("Editar", COR_BOTAO_AMARELO);
        btnEditar.addActionListener(e -> editarAluno());
        
        JButton btnExcluir = criarBotao("Excluir", COR_BOTAO_VERMELHO);
        btnExcluir.addActionListener(e -> excluirAluno());
        
        JButton btnVerMatricula = criarBotao("Ver Matrícula", COR_BOTAO_AZUL);
        btnVerMatricula.addActionListener(e -> verMatricula());
        
        JButton btnVerPagamentos = criarBotao("Pagamentos", COR_BOTAO_AZUL);
        btnVerPagamentos.addActionListener(e -> verPagamentos());
        
        JButton btnAvaliacao = criarBotao("Avaliação Física", COR_BOTAO_AZUL);
        btnAvaliacao.addActionListener(e -> abrirAvaliacaoFisica());
        
        panel.add(btnEditar);
        panel.add(btnExcluir);
        panel.add(btnVerMatricula);
        panel.add(btnVerPagamentos);
        panel.add(btnAvaliacao);
        
        return panel;
    }
    
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return botao;
    }
    
    private void carregarDados() {
        try {
            List<Aluno> alunos = alunoController.listarTodos();
            atualizarTabela(alunos);
            if (dashboard != null) {
                dashboard.atualizar();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar dados: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<Aluno> alunos) {
        modeloTabela.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Aluno aluno : alunos) {
            modeloTabela.addRow(new Object[]{
                aluno.getId(),
                aluno.getNomeCompleto(),
                aluno.getCpf(),
                aluno.getTelefone() != null ? aluno.getTelefone() : "",
                aluno.getEmail() != null ? aluno.getEmail() : "",
                aluno.getDataMatricula() != null ? aluno.getDataMatricula().format(formatter) : "",
                aluno.getStatus() != null ? aluno.getStatus().toString() : ""
            });
        }
    }
    
    private void pesquisarAlunos() {
        try {
            String termo = txtBusca.getText().trim();
            String tipoBusca = (String) cbTipoBusca.getSelectedItem();
            
            if (termo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um termo para pesquisar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            List<Aluno> alunos = new ArrayList<>();
            
            if (tipoBusca.equals("ID")) {
                try {
                    int id = Integer.parseInt(termo);
                    Aluno aluno = alunoController.buscarPorId(id);
                    if (aluno != null) {
                        alunos.add(aluno);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Digite um número válido para ID!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // Busca por Nome
                alunos = alunoController.buscarPorNome(termo);
            }
            
            if (alunos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum aluno encontrado!", "Busca", JOptionPane.INFORMATION_MESSAGE);
                modeloTabela.setRowCount(0);
            } else {
                atualizarTabela(alunos);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro na pesquisa: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void filtrarPorStatus() {
        String status = (String) cbFiltroStatus.getSelectedItem();
        
        if (status.equals("Todos")) {
            carregarDados();
            return;
        }
        
        try {
            StatusAluno statusAluno = StatusAluno.valueOf(status);
            List<Aluno> alunos = alunoController.buscarPorStatus(statusAluno);
            atualizarTabela(alunos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao filtrar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirFormularioAluno(Aluno aluno) {
        FormularioAluno dialog = new FormularioAluno((JFrame) SwingUtilities.getWindowAncestor(this), aluno);
        dialog.setVisible(true);
        carregarDados();
        if (dashboard != null) {
            dashboard.atualizar();
        }
    }
    
    private void editarAluno() {
        int selectedRow = tabelaAlunos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um aluno para editar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        try {
            Aluno aluno = alunoController.buscarPorId(id);
            abrirFormularioAluno(aluno);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar aluno: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirAluno() {
        int selectedRow = tabelaAlunos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um aluno para excluir",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este aluno?\nEsta ação não pode ser desfeita!",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabela.getValueAt(selectedRow, 0);
            try {
                alunoController.excluir(id);
                carregarDados();
                if (dashboard != null) {
                    dashboard.atualizar();
                }
                JOptionPane.showMessageDialog(this,
                    "Aluno excluído com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void verMatricula() {
        int selectedRow = tabelaAlunos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um aluno para ver a matrícula",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        try {
            Aluno aluno = alunoController.buscarPorId(id);
            System.out.println("Aluno selecionado: " + aluno.getNomeCompleto()); // Debug
            
            DialogMatricula dialog = new DialogMatricula((JFrame) SwingUtilities.getWindowAncestor(this), aluno);
            dialog.setVisible(true);
            
            carregarDados();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar matrícula: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verPagamentos() {
        int selectedRow = tabelaAlunos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um aluno para ver os pagamentos",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = (int) modeloTabela.getValueAt(selectedRow, 0);
            Aluno aluno = alunoController.buscarPorId(id);
            
            List<Pagamento> pagamentos = financeiroController.getPagamentosPorPeriodo(
                java.time.LocalDate.of(2020, 1, 1),
                java.time.LocalDate.now()
            );
            
            // Filtrar pagamentos do aluno
            List<Pagamento> pagamentosAluno = new ArrayList<>();
            double totalPago = 0;
            
            for (Pagamento p : pagamentos) {
                if (p.getAluno() != null && p.getAluno().getId() == aluno.getId()) {
                    pagamentosAluno.add(p);
                    totalPago += p.getValor();
                }
            }
            
            if (pagamentosAluno.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Aluno não possui pagamentos registrados!",
                    "Pagamentos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("╔═══════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                    HISTÓRICO DE PAGAMENTOS                         ║\n");
            sb.append("╠═══════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ Aluno: ").append(aluno.getNomeCompleto()).append("\n");
            sb.append("╠═══════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ Data        │ Valor    │ Forma          │ Status                 ║\n");
            sb.append("║─────────────┼──────────┼────────────────┼────────────────────────║\n");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Pagamento p : pagamentosAluno) {
                String forma = "";
                switch (p.getFormaPagamento()) {
                    case PIX: forma = "PIX"; break;
                    case DINHEIRO: forma = "Dinheiro"; break;
                    case CARTAO_CREDITO: forma = "Cartão Crédito"; break;
                    case CARTAO_DEBITO: forma = "Cartão Débito"; break;
                    case TRANSFERENCIA: forma = "Transferência"; break;
                }
                sb.append(String.format("║ %-10s │ R$ %6.2f │ %-14s │ %-22s ║\n",
                    p.getDataPagamento().format(formatter),
                    p.getValor(),
                    forma,
                    p.getStatus()));
            }
            
            sb.append("╠═══════════════════════════════════════════════════════════════════╣\n");
            sb.append(String.format("║ TOTAL PAGO: R$ %.2f%38s║\n", totalPago, ""));
            sb.append("╚═══════════════════════════════════════════════════════════════════╝");
            
            JOptionPane.showMessageDialog(this, sb.toString(), "Pagamentos do Aluno", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar pagamentos: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirAvaliacaoFisica() {
        int selectedRow = tabelaAlunos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um aluno para fazer avaliação física",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        try {
            Aluno aluno = alunoController.buscarPorId(id);
            DialogAvaliacaoFisica dialog = new DialogAvaliacaoFisica((JFrame) SwingUtilities.getWindowAncestor(this), aluno);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}