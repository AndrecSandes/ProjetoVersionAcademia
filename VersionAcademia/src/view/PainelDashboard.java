package view;

import controller.AlunoController;
import controller.FinanceiroController;
import model.Aluno;
import model.Pagamento;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelDashboard extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_CARD = Color.WHITE;
    
    private AlunoController alunoController;
    private FinanceiroController financeiroController;
    
    private JLabel lblTotalAlunos;
    private JLabel lblReceitaMensal;
    private JLabel lblMatriculasAtivas;
    private JLabel lblInadimplencia;
    private JLabel lblPlanosVendidos;
    private JLabel lblTotalGeral;
    
    public PainelDashboard() {
        alunoController = new AlunoController();
        financeiroController = new FinanceiroController();
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // CABEÇALHO
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COR_FUNDO);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel lblTitulo = new JLabel("PAINEL DE CONTROLE");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(44, 62, 80));
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        
        JLabel lblData = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy")));
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblData.setForeground(Color.GRAY);
        headerPanel.add(lblData, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // CARDS PRINCIPAIS
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        // Card 1 - Total de Alunos
        JPanel card1 = criarCardAzul("TOTAL DE ALUNOS", "0", "Todos os alunos cadastrados");
        lblTotalAlunos = (JLabel) ((JPanel) card1.getComponent(0)).getComponent(1);
        
        // Card 2 - Receita Mensal
        JPanel card2 = criarCardVerde("RECEITA DO MÊS", "R$ 0,00", LocalDate.now().getMonth().toString());
        lblReceitaMensal = (JLabel) ((JPanel) card2.getComponent(0)).getComponent(1);
        
        // Card 3 - Matrículas Ativas
        JPanel card3 = criarCardAzulClaro("MATRÍCULAS ATIVAS", "0", "Alunos com plano ativo");
        lblMatriculasAtivas = (JLabel) ((JPanel) card3.getComponent(0)).getComponent(1);
        
        // Card 4 - Inadimplência
        JPanel card4 = criarCardVermelho("INADIMPLÊNCIA", "0%", "Mensalidades atrasadas");
        lblInadimplencia = (JLabel) ((JPanel) card4.getComponent(0)).getComponent(1);
        
        // Card 5 - Planos Vendidos
        JPanel card5 = criarCardRoxo("PLANOS VENDIDOS", "0", "No mês atual");
        lblPlanosVendidos = (JLabel) ((JPanel) card5.getComponent(0)).getComponent(1);
        
        // Card 6 - Total Arrecadado
        JPanel card6 = criarCardDourado("TOTAL ACUMULADO", "R$ 0,00", "Histórico completo");
        lblTotalGeral = (JLabel) ((JPanel) card6.getComponent(0)).getComponent(1);
        
        cardsPanel.add(card1);
        cardsPanel.add(card2);
        cardsPanel.add(card3);
        cardsPanel.add(card4);
        cardsPanel.add(card5);
        cardsPanel.add(card6);
        
        add(cardsPanel, BorderLayout.CENTER);
        
        // RODAPÉ COM ALERTAS
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(COR_FUNDO);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel lblAlertas = new JLabel("ATENÇÃO");
        lblAlertas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAlertas.setForeground(new Color(231, 76, 60));
        lblAlertas.setAlignmentX(Component.LEFT_ALIGNMENT);
        footerPanel.add(lblAlertas);
        
        footerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JTextArea txtAlertas = new JTextArea();
        txtAlertas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAlertas.setBackground(COR_FUNDO);
        txtAlertas.setEditable(false);
        txtAlertas.setForeground(Color.DARK_GRAY);
        txtAlertas.setText("• Alunos com pagamento atrasado precisam ser contactados\n• Verificar matrículas que vencem nos próximos 7 dias\n• Manter a taxa de inadimplência abaixo de 10%");
        footerPanel.add(txtAlertas);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel criarCardAzul(String titulo, String valor, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(52, 152, 219));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        textPanel.add(lblSubtitulo);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarCardVerde(String titulo, String valor, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(46, 204, 113));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        textPanel.add(lblSubtitulo);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarCardAzulClaro(String titulo, String valor, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(41, 128, 185));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        textPanel.add(lblSubtitulo);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarCardVermelho(String titulo, String valor, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(231, 76, 60));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        textPanel.add(lblSubtitulo);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarCardRoxo(String titulo, String valor, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(155, 89, 182));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        textPanel.add(lblSubtitulo);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarCardDourado(String titulo, String valor, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(241, 196, 15), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(241, 196, 15));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        textPanel.add(lblSubtitulo);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void carregarDados() {
        try {
            // Total de alunos
            List<Aluno> alunos = alunoController.listarTodos();
            int totalAlunos = alunos.size();
            lblTotalAlunos.setText(String.valueOf(totalAlunos));
            
            // Alunos ativos
            long ativos = alunos.stream().filter(a -> a.getStatus() == model.Aluno.StatusAluno.ATIVO).count();
            lblMatriculasAtivas.setText(String.valueOf(ativos));
            
            // Receita do mês atual
            LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
            LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            double receitaMensal = financeiroController.getTotalArrecadadoPorPeriodo(inicioMes, fimMes);
            lblReceitaMensal.setText(String.format("R$ %.2f", receitaMensal));
            
            // Total geral acumulado
            List<Pagamento> todosPagamentos = financeiroController.getPagamentosPorPeriodo(
                LocalDate.of(2020, 1, 1), LocalDate.now());
            double totalGeral = todosPagamentos.stream().mapToDouble(Pagamento::getValor).sum();
            lblTotalGeral.setText(String.format("R$ %.2f", totalGeral));
            
            // Inadimplência: alunos INATIVOS
            long inadimplentes = alunos.stream().filter(a -> a.getStatus() == model.Aluno.StatusAluno.INATIVO).count();
            double percentualInadimplencia = totalAlunos > 0 ? (inadimplentes * 100.0 / totalAlunos) : 0;
            lblInadimplencia.setText(String.format("%.1f%%", percentualInadimplencia));
            
            // Planos vendidos no mês
            List<Pagamento> pagamentosMes = financeiroController.getPagamentosPorPeriodo(inicioMes, fimMes);
            lblPlanosVendidos.setText(String.valueOf(pagamentosMes.size()));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void atualizar() {
        carregarDados();
    }
}