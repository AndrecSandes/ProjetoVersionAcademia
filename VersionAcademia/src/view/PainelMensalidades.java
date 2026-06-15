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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PainelMensalidades extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private FinanceiroController financeiroController;
    private AlunoController alunoController;
    
    private JTable tabelaMensalidades;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> cbMes;
    private JComboBox<String> cbAno;
    private JLabel lblTotalMensalidades;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_PRIMARIA = new Color(41, 128, 185);
    private final Color COR_PAGO = new Color(46, 204, 113);
    private final Color COR_PENDENTE = new Color(241, 196, 15);
    private final Color COR_ATRASADO = new Color(231, 76, 60);
    
    public PainelMensalidades() {
        financeiroController = new FinanceiroController();
        alunoController = new AlunoController();
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("Controle de Mensalidades", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(44, 62, 80));
        add(titulo, BorderLayout.NORTH);
        
        JPanel filtroPanel = criarFiltro();
        add(filtroPanel, BorderLayout.CENTER);
        
        JPanel tablePanel = criarPainelTabela();
        add(tablePanel, BorderLayout.SOUTH);
    }
    
    private JPanel criarFiltro() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblMes = new JLabel("Mês:");
        lblMes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblMes);
        
        cbMes = new JComboBox<>(new String[]{
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        });
        cbMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        panel.add(cbMes);
        
        JLabel lblAno = new JLabel("Ano:");
        lblAno.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblAno);
        
        cbAno = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual - 2; i <= anoAtual + 1; i++) {
            cbAno.addItem(String.valueOf(i));
        }
        cbAno.setSelectedItem(String.valueOf(anoAtual));
        panel.add(cbAno);
        
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(COR_PRIMARIA);
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFiltrar.addActionListener(e -> carregarMensalidades());
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
        
        String[] colunas = {"ID", "Aluno", "Valor Mensalidade", "Status", "Último Pagamento"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaMensalidades = new JTable(modeloTabela);
        
        tabelaMensalidades.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaMensalidades.getTableHeader().setBackground(new Color(44, 62, 80));
        tabelaMensalidades.getTableHeader().setForeground(Color.WHITE);
        tabelaMensalidades.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaMensalidades.setRowHeight(30);
        
        // Renderizador de cores para o status
        tabelaMensalidades.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected && column == 3) {
                    String status = (String) table.getValueAt(row, 3);
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
        
        JScrollPane scrollPane = new JScrollPane(tabelaMensalidades);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Label de total
        lblTotalMensalidades = new JLabel("Total do período: R$ 0,00");
        lblTotalMensalidades.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalMensalidades.setForeground(COR_PRIMARIA);
        lblTotalMensalidades.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(lblTotalMensalidades, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void carregarDados() {
        carregarMensalidades();
    }
    
    private void carregarMensalidades() {
        try {
            modeloTabela.setRowCount(0);
            
            int mes = cbMes.getSelectedIndex() + 1;
            int ano = Integer.parseInt((String) cbAno.getSelectedItem());
            
            LocalDate inicio = LocalDate.of(ano, mes, 1);
            LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());
            
            // Buscar pagamentos do período
            List<Pagamento> pagamentos = financeiroController.getPagamentosPorPeriodo(inicio, fim);
            
            // Mapa de pagamentos por aluno
            Map<Integer, Pagamento> pagamentosMap = new HashMap<>();
            for (Pagamento p : pagamentos) {
                if (p.getAluno() != null) {
                    pagamentosMap.put(p.getAluno().getId(), p);
                }
            }
            
            // Todos os alunos ativos
            List<Aluno> alunos = alunoController.listarTodos();
            double valorTotalPeriodo = 0;
            
            for (Aluno a : alunos) {
                Pagamento p = pagamentosMap.get(a.getId());
                String status;
                String ultimoPagamento;
                double valor;
                
                if (p != null) {
                    status = p.getStatus().toString();
                    ultimoPagamento = p.getDataPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    valor = p.getValor();
                    valorTotalPeriodo += valor;
                } else {
                    // Verificar se o aluno está inadimplente
                    if (a.getStatus() == Aluno.StatusAluno.INATIVO) {
                        status = "INADIMPLENTE";
                    } else if (a.getStatus() == Aluno.StatusAluno.PENDENTE) {
                        status = "PENDENTE";
                    } else {
                        status = "ATRASADO";
                    }
                    ultimoPagamento = "Nenhum";
                    valor = 0;
                }
                
                modeloTabela.addRow(new Object[]{
                    a.getId(),
                    a.getNomeCompleto(),
                    String.format("R$ %.2f", valor),
                    status,
                    ultimoPagamento
                });
            }
            
            lblTotalMensalidades.setText(String.format("Total arrecadado em %s/%d: R$ %.2f", 
                cbMes.getSelectedItem(), ano, valorTotalPeriodo));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar mensalidades: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void atualizar() {
        carregarDados();
    }
}