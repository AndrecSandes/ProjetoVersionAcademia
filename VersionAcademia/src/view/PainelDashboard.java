package view;

import controller.AlunoController;
import controller.FinanceiroController;
import model.Aluno;
import model.Pagamento;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
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
        
        JLabel titulo = new JLabel("Dashboard", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 5, 20, 20));
        cardsPanel.setOpaque(false);
        
        JPanel card1 = criarCard("Total de Alunos", "0", new Color(41, 128, 185));
        lblTotalAlunos = (JLabel) ((JPanel) card1.getComponent(0)).getComponent(1);
        
        JPanel card2 = criarCard("Receita Mensal", "R$ 0,00", new Color(46, 204, 113));
        lblReceitaMensal = (JLabel) ((JPanel) card2.getComponent(0)).getComponent(1);
        
        JPanel card3 = criarCard("Matrículas Ativas", "0", new Color(52, 152, 219));
        lblMatriculasAtivas = (JLabel) ((JPanel) card3.getComponent(0)).getComponent(1);
        
        JPanel card4 = criarCard("Inadimplência", "0%", new Color(231, 76, 60));
        lblInadimplencia = (JLabel) ((JPanel) card4.getComponent(0)).getComponent(1);
        
        JPanel card5 = criarCard("Planos Vendidos", "0", new Color(155, 89, 182));
        lblPlanosVendidos = (JLabel) ((JPanel) card5.getComponent(0)).getComponent(1);
        
        cardsPanel.add(card1);
        cardsPanel.add(card2);
        cardsPanel.add(card3);
        cardsPanel.add(card4);
        cardsPanel.add(card5);
        
        add(titulo, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
    }
    
    private JPanel criarCard(String titulo, String valor, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(cor);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        
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
            
            // Receita do mês atual (baseado nos pagamentos registrados)
            LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
            LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            double receitaMensal = financeiroController.getTotalArrecadadoPorPeriodo(inicioMes, fimMes);
            lblReceitaMensal.setText(String.format("R$ %.2f", receitaMensal));
            
            // Inadimplência: alunos INATIVOS (não pagaram)
            long inadimplentes = alunos.stream().filter(a -> a.getStatus() == model.Aluno.StatusAluno.INATIVO).count();
            double percentualInadimplencia = totalAlunos > 0 ? (inadimplentes * 100.0 / totalAlunos) : 0;
            lblInadimplencia.setText(String.format("%.1f%%", percentualInadimplencia));
            
            // Planos vendidos (total de pagamentos do mês)
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