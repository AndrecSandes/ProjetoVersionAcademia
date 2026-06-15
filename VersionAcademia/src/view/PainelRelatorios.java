package view;

import controller.RelatorioController;
import model.Aluno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class PainelRelatorios extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private RelatorioController relatorioController;
    
    private JTabbedPane tabbedPane;
    
    // Componentes da aba Alunos
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabelaAlunos;
    
    // Componentes da aba Financeiro
    private JTable tabelaFinanceiro;
    private DefaultTableModel modeloTabelaFinanceiro;
    private JComboBox<String> cbAnoFinanceiro;
    private JLabel lblTotalFinanceiro;
    
    // Componentes da aba Frequência
    private JTable tabelaFrequencia;
    private DefaultTableModel modeloTabelaFrequencia;
    private JComboBox<String> cbMesFrequencia;
    private JComboBox<String> cbAnoFrequencia;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_BOTAO_AZUL = new Color(41, 128, 185);
    private final Color COR_BOTAO_VERDE = new Color(46, 204, 113);
    private final Color COR_BOTAO_AMARELO = new Color(241, 196, 15);
    
    public PainelRelatorios() {
        relatorioController = new RelatorioController();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("Relatórios Gerenciais", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Abas
        tabbedPane.addTab("Alunos", criarPainelAlunos());
        tabbedPane.addTab("Financeiro", criarPainelFinanceiro());
        tabbedPane.addTab("Frequência", criarPainelFrequencia());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // ==================== PAINEL ALUNOS ====================
    
    private JPanel criarPainelAlunos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COR_FUNDO);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        botoesPanel.setBackground(COR_FUNDO);
        
        JButton btnAtivos = criarBotaoRelatorio("Alunos Ativos", COR_BOTAO_VERDE);
        btnAtivos.addActionListener(e -> gerarRelatorioAlunosPorStatus("ATIVO"));
        
        JButton btnInativos = criarBotaoRelatorio("Alunos Inativos", COR_BOTAO_AMARELO);
        btnInativos.addActionListener(e -> gerarRelatorioAlunosPorStatus("INATIVO"));
        
        JButton btnPendentes = criarBotaoRelatorio("Alunos Pendentes", COR_BOTAO_AZUL);
        btnPendentes.addActionListener(e -> gerarRelatorioAlunosPorStatus("PENDENTE"));
        
        JButton btnMatriculasVencidas = criarBotaoRelatorio("Matrículas Vencidas", new Color(231, 76, 60));
        btnMatriculasVencidas.addActionListener(e -> gerarRelatorioMatriculasVencidas());
        
        botoesPanel.add(btnAtivos);
        botoesPanel.add(btnInativos);
        botoesPanel.add(btnPendentes);
        botoesPanel.add(btnMatriculasVencidas);
        
        // Tabela
        JPanel tablePanel = criarPainelTabelaAlunos();
        
        panel.add(botoesPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelTabelaAlunos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Email", "Data Matrícula", "Status"};
        modeloTabelaAlunos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaAlunos = new JTable(modeloTabelaAlunos);
        configurarTabela(tabelaAlunos);
        
        JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void gerarRelatorioAlunosPorStatus(String status) {
        try {
            modeloTabelaAlunos.setRowCount(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            List<Aluno> alunos = relatorioController.getAlunosPorStatus(status);
            
            for (Aluno a : alunos) {
                modeloTabelaAlunos.addRow(new Object[]{
                    a.getId(),
                    a.getNomeCompleto(),
                    a.getCpf(),
                    a.getTelefone() != null ? a.getTelefone() : "",
                    a.getEmail() != null ? a.getEmail() : "",
                    a.getDataMatricula().format(formatter),
                    a.getStatus()
                });
            }
            
            String titulo = "";
            switch (status) {
                case "ATIVO": titulo = "Ativos"; break;
                case "INATIVO": titulo = "Inativos"; break;
                case "PENDENTE": titulo = "Pendentes"; break;
            }
            
            JOptionPane.showMessageDialog(this,
                "Total de Alunos " + titulo + ": " + alunos.size(),
                "Resumo", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorioMatriculasVencidas() {
        try {
            modeloTabelaAlunos.setRowCount(0);
            
            String[] colunas = {"ID", "Nº Matrícula", "Aluno", "CPF", "Telefone", "Plano", "Vencimento"};
            modeloTabelaAlunos.setColumnIdentifiers(colunas);
            
            List<Map<String, Object>> matriculas = relatorioController.getMatriculasVencidas();
            
            for (Map<String, Object> m : matriculas) {
                modeloTabelaAlunos.addRow(new Object[]{
                    m.get("id"),
                    m.get("numero_matricula"),
                    m.get("aluno_nome"),
                    m.get("aluno_cpf"),
                    m.get("aluno_telefone"),
                    m.get("plano_nome"),
                    m.get("proximo_vencimento")
                });
            }
            
            JOptionPane.showMessageDialog(this,
                "Total de Matrículas Vencidas: " + matriculas.size(),
                "Resumo", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== PAINEL FINANCEIRO ====================
    
    private JPanel criarPainelFinanceiro() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COR_FUNDO);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtrosPanel.setBackground(COR_FUNDO);
        
        JLabel lblAno = new JLabel("Ano:");
        lblAno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filtrosPanel.add(lblAno);
        
        cbAnoFinanceiro = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual - 2; i <= anoAtual + 1; i++) {
            cbAnoFinanceiro.addItem(String.valueOf(i));
        }
        cbAnoFinanceiro.setSelectedItem(String.valueOf(anoAtual));
        filtrosPanel.add(cbAnoFinanceiro);
        
        JButton btnGerarReceitaMensal = criarBotaoRelatorio("Receita Mensal", COR_BOTAO_AZUL);
        btnGerarReceitaMensal.addActionListener(e -> gerarRelatorioReceitaMensal());
        
        JButton btnGerarReceitaAnual = criarBotaoRelatorio("Receita Anual", COR_BOTAO_VERDE);
        btnGerarReceitaAnual.addActionListener(e -> gerarRelatorioReceitaAnual());
        
        JButton btnPlanosMaisVendidos = criarBotaoRelatorio("Planos Mais Vendidos", COR_BOTAO_AMARELO);
        btnPlanosMaisVendidos.addActionListener(e -> gerarRelatorioPlanosMaisVendidos());
        
        filtrosPanel.add(btnGerarReceitaMensal);
        filtrosPanel.add(btnGerarReceitaAnual);
        filtrosPanel.add(btnPlanosMaisVendidos);
        
        // Total
        lblTotalFinanceiro = new JLabel("Total: R$ 0,00");
        lblTotalFinanceiro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalFinanceiro.setForeground(COR_BOTAO_VERDE);
        lblTotalFinanceiro.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Tabela
        JPanel tablePanel = criarPainelTabelaFinanceiro();
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COR_FUNDO);
        topPanel.add(filtrosPanel, BorderLayout.NORTH);
        topPanel.add(lblTotalFinanceiro, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelTabelaFinanceiro() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        modeloTabelaFinanceiro = new DefaultTableModel(new String[]{"Período", "Quantidade", "Valor Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaFinanceiro = new JTable(modeloTabelaFinanceiro);
        configurarTabela(tabelaFinanceiro);
        
        JScrollPane scrollPane = new JScrollPane(tabelaFinanceiro);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void gerarRelatorioReceitaMensal() {
        try {
            modeloTabelaFinanceiro.setRowCount(0);
            
            int ano = Integer.parseInt((String) cbAnoFinanceiro.getSelectedItem());
            List<Map<String, Object>> receitas = relatorioController.getReceitaMensal(ano);
            
            String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                              "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
            
            double totalGeral = 0;
            
            for (Map<String, Object> r : receitas) {
                int mes = (int) r.get("mes");
                double total = (double) r.get("total");
                int qtd = (int) r.get("quantidade");
                
                modeloTabelaFinanceiro.addRow(new Object[]{
                    meses[mes - 1],
                    qtd,
                    String.format("R$ %.2f", total)
                });
                totalGeral += total;
            }
            
            lblTotalFinanceiro.setText(String.format("Total do Ano: R$ %.2f", totalGeral));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorioReceitaAnual() {
        try {
            modeloTabelaFinanceiro.setRowCount(0);
            
            List<Map<String, Object>> receitas = relatorioController.getReceitaAnual();
            double totalGeral = 0;
            
            for (Map<String, Object> r : receitas) {
                int ano = (int) r.get("ano");
                double total = (double) r.get("total");
                int qtd = (int) r.get("quantidade");
                
                modeloTabelaFinanceiro.addRow(new Object[]{
                    ano,
                    qtd,
                    String.format("R$ %.2f", total)
                });
                totalGeral += total;
            }
            
            lblTotalFinanceiro.setText(String.format("Total Geral: R$ %.2f", totalGeral));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorioPlanosMaisVendidos() {
        try {
            modeloTabelaFinanceiro.setRowCount(0);
            modeloTabelaFinanceiro.setColumnIdentifiers(new String[]{"Plano", "Valor", "Alunos Matriculados"});
            
            List<Map<String, Object>> planos = relatorioController.getPlanosMaisVendidos();
            
            for (Map<String, Object> p : planos) {
                modeloTabelaFinanceiro.addRow(new Object[]{
                    p.get("nome"),
                    String.format("R$ %.2f", p.get("valor")),
                    p.get("total_alunos")
                });
            }
            
            lblTotalFinanceiro.setText("Total de Planos: " + planos.size());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== PAINEL FREQUÊNCIA ====================
    
    private JPanel criarPainelFrequencia() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COR_FUNDO);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtrosPanel.setBackground(COR_FUNDO);
        
        JLabel lblMes = new JLabel("Mês:");
        lblMes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filtrosPanel.add(lblMes);
        
        cbMesFrequencia = new JComboBox<>(new String[]{
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        });
        cbMesFrequencia.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        filtrosPanel.add(cbMesFrequencia);
        
        JLabel lblAno = new JLabel("Ano:");
        lblAno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filtrosPanel.add(lblAno);
        
        cbAnoFrequencia = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for (int i = anoAtual - 2; i <= anoAtual + 1; i++) {
            cbAnoFrequencia.addItem(String.valueOf(i));
        }
        cbAnoFrequencia.setSelectedItem(String.valueOf(anoAtual));
        filtrosPanel.add(cbAnoFrequencia);
        
        JButton btnGerarFrequencia = criarBotaoRelatorio("Gerar Frequência", COR_BOTAO_AZUL);
        btnGerarFrequencia.addActionListener(e -> gerarRelatorioFrequencia());
        filtrosPanel.add(btnGerarFrequencia);
        
        // Tabela
        JPanel tablePanel = criarPainelTabelaFrequencia();
        
        panel.add(filtrosPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelTabelaFrequencia() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        String[] colunas = {"ID", "Aluno", "CPF", "Frequência (dias/mês)", "Classificação"};
        modeloTabelaFrequencia = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaFrequencia = new JTable(modeloTabelaFrequencia);
        configurarTabela(tabelaFrequencia);
        
        JScrollPane scrollPane = new JScrollPane(tabelaFrequencia);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void gerarRelatorioFrequencia() {
        try {
            modeloTabelaFrequencia.setRowCount(0);
            
            int mes = cbMesFrequencia.getSelectedIndex() + 1;
            int ano = Integer.parseInt((String) cbAnoFrequencia.getSelectedItem());
            
            List<Map<String, Object>> frequencias = relatorioController.getFrequenciaAlunos(mes, ano);
            
            for (Map<String, Object> f : frequencias) {
                modeloTabelaFrequencia.addRow(new Object[]{
                    f.get("id"),
                    f.get("nome"),
                    f.get("cpf"),
                    f.get("frequencia"),
                    f.get("classificacao")
                });
            }
            
            // Calcula estatísticas
            long excelente = frequencias.stream().filter(f -> "Excelente".equals(f.get("classificacao"))).count();
            long regular = frequencias.stream().filter(f -> "Regular".equals(f.get("classificacao"))).count();
            long baixa = frequencias.stream().filter(f -> "Baixa".equals(f.get("classificacao"))).count();
            long semPresenca = frequencias.stream().filter(f -> "Sem presença".equals(f.get("classificacao"))).count();
            
            JOptionPane.showMessageDialog(this,
                "Resumo de Frequência - " + cbMesFrequencia.getSelectedItem() + "/" + ano + "\n" +
                "Excelente (4+ dias): " + excelente + " alunos\n" +
                "Regular (2-3 dias): " + regular + " alunos\n" +
                "Baixa (1 dia): " + baixa + " alunos\n" +
                "Sem presença: " + semPresenca + " alunos",
                "Resumo", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== UTILITÁRIOS ====================
    
    private JButton criarBotaoRelatorio(String texto, Color cor) {
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
    
    private void configurarTabela(JTable tabela) {
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(44, 62, 80));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setReorderingAllowed(false);
        
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.setRowHeight(30);
        tabela.setForeground(new Color(51, 51, 51));
        tabela.setBackground(Color.WHITE);
        tabela.setSelectionBackground(COR_BOTAO_AZUL);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(COR_BORDA);
        tabela.setShowVerticalLines(true);
    }
}