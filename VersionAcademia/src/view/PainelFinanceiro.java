package view;

import controller.FinanceiroController;
import controller.AlunoController;
import model.Pagamento;
import model.Aluno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelFinanceiro extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private FinanceiroController financeiroController;
    private AlunoController alunoController;
    
    private JLabel lblTotalReceitas;
    private JLabel lblReceitaMes;
    private JLabel lblReceitaAno;
    private JLabel lblTicketMedio;
    private JLabel lblInadimplenciaFinanceiro;
    
    private JTable tabelaPagamentos;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> cbMesFiltro;
    private JComboBox<String> cbAnoFiltro;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_SUCESSO = new Color(46, 204, 113);
    private final Color COR_PERIGO = new Color(231, 76, 60);
    private final Color COR_PRIMARIA = new Color(41, 128, 185);
    
    public PainelFinanceiro() {
        financeiroController = new FinanceiroController();
        alunoController = new AlunoController();
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        // Painel superior com cards
        JPanel topPanel = criarPainelCards();
        add(topPanel, BorderLayout.NORTH);
        
        // Painel de filtros
        JPanel filtroPanel = criarFiltro();
        add(filtroPanel, BorderLayout.CENTER);
        
        // Tabela de pagamentos
        JPanel tablePanel = criarPainelTabela();
        add(tablePanel, BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelCards() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 15, 15));
        panel.setBackground(COR_FUNDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        lblTotalReceitas = new JLabel("R$ 0,00");
        lblReceitaMes = new JLabel("R$ 0,00");
        lblReceitaAno = new JLabel("R$ 0,00");
        lblTicketMedio = new JLabel("R$ 0,00");
        lblInadimplenciaFinanceiro = new JLabel("0%");
        
        panel.add(criarCard("Total Geral", lblTotalReceitas, COR_PRIMARIA));
        panel.add(criarCard("Receita do Mês", lblReceitaMes, COR_SUCESSO));
        panel.add(criarCard("Receita do Ano", lblReceitaAno, COR_PRIMARIA));
        panel.add(criarCard("Ticket Médio", lblTicketMedio, COR_SUCESSO));
        panel.add(criarCard("Inadimplência", lblInadimplenciaFinanceiro, COR_PERIGO));
        
        return panel;
    }
    
    private JPanel criarCard(String titulo, JLabel lblValor, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COR_PAINEL_BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(cor);
        
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarFiltro() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblMes = new JLabel("Mês:");
        lblMes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblMes);
        
        cbMesFiltro = new JComboBox<>(new String[]{
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        });
        cbMesFiltro.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        panel.add(cbMesFiltro);
        
        JLabel lblAno = new JLabel("Ano:");
        lblAno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblAno);
        
        cbAnoFiltro = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual - 2; i <= anoAtual + 1; i++) {
            cbAnoFiltro.addItem(String.valueOf(i));
        }
        cbAnoFiltro.setSelectedItem(String.valueOf(anoAtual));
        panel.add(cbAnoFiltro);
        
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(COR_PRIMARIA);
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFiltrar.addActionListener(e -> filtrarPagamentos());
        panel.add(btnFiltrar);
        
        return panel;
    }
    
    private JPanel criarPainelTabela() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        String[] colunas = {"ID", "Aluno", "Valor", "Data Pagamento", "Forma", "Competência", "Status"};
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
        
        JScrollPane scrollPane = new JScrollPane(tabelaPagamentos);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void carregarDados() {
        try {
            // Total Geral
            List<Pagamento> todosPagamentos = financeiroController.getPagamentosPorPeriodo(
                LocalDate.of(2020, 1, 1), LocalDate.now());
            double totalGeral = todosPagamentos.stream().mapToDouble(Pagamento::getValor).sum();
            lblTotalReceitas.setText(String.format("R$ %.2f", totalGeral));
            
            // Receita do mês atual
            LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
            LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            double receitaMes = financeiroController.getTotalArrecadadoPorPeriodo(inicioMes, fimMes);
            lblReceitaMes.setText(String.format("R$ %.2f", receitaMes));
            
            // Receita do ano
            LocalDate inicioAno = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            LocalDate fimAno = LocalDate.of(LocalDate.now().getYear(), 12, 31);
            double receitaAno = financeiroController.getTotalArrecadadoPorPeriodo(inicioAno, fimAno);
            lblReceitaAno.setText(String.format("R$ %.2f", receitaAno));
            
            // Ticket médio
            int totalAlunos = alunoController.listarTodos().size();
            double ticketMedio = totalAlunos > 0 ? totalGeral / totalAlunos : 0;
            lblTicketMedio.setText(String.format("R$ %.2f", ticketMedio));
            
            // Inadimplência
            long inadimplentes = alunoController.listarTodos().stream()
                .filter(a -> a.getStatus() == model.Aluno.StatusAluno.INATIVO).count();
            double percentual = totalAlunos > 0 ? (inadimplentes * 100.0 / totalAlunos) : 0;
            lblInadimplenciaFinanceiro.setText(String.format("%.1f%%", percentual));
            
            // Carregar tabela com filtro atual
            filtrarPagamentos();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void filtrarPagamentos() {
        try {
            int mes = cbMesFiltro.getSelectedIndex() + 1;
            int ano = Integer.parseInt((String) cbAnoFiltro.getSelectedItem());
            
            LocalDate inicio = LocalDate.of(ano, mes, 1);
            LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());
            
            List<Pagamento> pagamentos = financeiroController.getPagamentosPorPeriodo(inicio, fim);
            
            modeloTabela.setRowCount(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
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
    
    public void atualizar() {
        carregarDados();
    }
}