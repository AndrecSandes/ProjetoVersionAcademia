package view;

import controller.AlunoController;
import controller.FinanceiroController;
import model.Aluno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PainelEstatisticas extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_CARD = Color.WHITE;
    private final Color COR_PRIMARIA = new Color(41, 128, 185);
    private final Color COR_SUCESSO = new Color(46, 204, 113);
    private final Color COR_PERIGO = new Color(231, 76, 60);
    
    private AlunoController alunoController;
    private FinanceiroController financeiroController;
    
    private JLabel lblTotalAlunos;
    private JLabel lblAlunosAtivos;
    private JLabel lblAlunosInativos;
    private JLabel lblAlunosPendentes;
    private JLabel lblTicketMedio;
    private JLabel lblFaturamentoMes;
    private JLabel lblInadimplencia;
    
    public PainelEstatisticas() {
        alunoController = new AlunoController();
        financeiroController = new FinanceiroController();
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COR_FUNDO);
        
        // Título
        JLabel titulo = new JLabel("Estatísticas da Academia", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(0, 0, 30, 0));
        mainPanel.add(titulo);
        
        // Seção 1: Alunos
        mainPanel.add(criarSecaoTitulo("Total de Alunos"));
        JPanel alunosPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        alunosPanel.setBackground(COR_FUNDO);
        alunosPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        lblTotalAlunos = new JLabel("0");
        lblAlunosAtivos = new JLabel("0");
        lblAlunosInativos = new JLabel("0");
        lblAlunosPendentes = new JLabel("0");
        
        alunosPanel.add(criarCard("Total", lblTotalAlunos, COR_PRIMARIA));
        alunosPanel.add(criarCard("Ativos", lblAlunosAtivos, COR_SUCESSO));
        alunosPanel.add(criarCard("Inativos", lblAlunosInativos, COR_PERIGO));
        alunosPanel.add(criarCard("Pendentes", lblAlunosPendentes, new Color(241, 196, 15)));
        
        mainPanel.add(alunosPanel);
        
        // Seção 2: Financeiro
        mainPanel.add(criarSecaoTitulo("Financeiro"));
        JPanel financeiroPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        financeiroPanel.setBackground(COR_FUNDO);
        financeiroPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        lblTicketMedio = new JLabel("R$ 0,00");
        lblFaturamentoMes = new JLabel("R$ 0,00");
        lblInadimplencia = new JLabel("0%");
        
        financeiroPanel.add(criarCard("Ticket Médio", lblTicketMedio, COR_PRIMARIA));
        financeiroPanel.add(criarCard("Faturamento Mensal", lblFaturamentoMes, COR_SUCESSO));
        financeiroPanel.add(criarCard("Inadimplência", lblInadimplencia, COR_PERIGO));
        
        mainPanel.add(financeiroPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel criarSecaoTitulo(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_FUNDO);
        panel.setBorder(new EmptyBorder(15, 0, 10, 0));
        
        JLabel label = new JLabel(titulo);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(44, 62, 80));
        
        panel.add(label, BorderLayout.WEST);
        return panel;
    }
    
    private JPanel criarCard(String titulo, JLabel lblValor, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(cor);
        
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblValor);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void carregarDados() {
        try {
            // Dados de Alunos (reais do banco)
            List<Aluno> alunos = alunoController.listarTodos();
            int total = alunos.size();
            int ativos = 0, inativos = 0, pendentes = 0;
            
            for (Aluno a : alunos) {
                switch (a.getStatus()) {
                    case ATIVO: ativos++; break;
                    case INATIVO: inativos++; break;
                    case PENDENTE: pendentes++; break;
                }
            }
            
            lblTotalAlunos.setText(String.valueOf(total));
            lblAlunosAtivos.setText(String.valueOf(ativos));
            lblAlunosInativos.setText(String.valueOf(inativos));
            lblAlunosPendentes.setText(String.valueOf(pendentes));
            
            // Dados Financeiros (reais do banco)
            double faturamento = financeiroController.getFaturamentoTotalMes(LocalDate.now());
            lblFaturamentoMes.setText(String.format("R$ %.2f", faturamento));
            
            double ticketMedio = financeiroController.getTicketMedio();
            lblTicketMedio.setText(String.format("R$ %.2f", ticketMedio));
            
            double inadimplencia = financeiroController.getPercentualInadimplencia();
            lblInadimplencia.setText(String.format("%.1f%%", inadimplencia));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar estatísticas: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void atualizar() {
        carregarDados();
    }
}