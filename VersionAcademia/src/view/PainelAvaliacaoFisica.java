package view;

import controller.AlunoController;
import model.Aluno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PainelAvaliacaoFisica extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private AlunoController alunoController;
    private JTextField txtBuscaAluno;
    private JLabel lblAlunoNome;
    private JTextField txtPeso;
    private JTextField txtAltura;
    private JTextField txtPercentualGordura;
    private JTextField txtMassaMuscular;
    private JTextArea txtObservacoes;
    private JList<String> listaHistorico;
    private DefaultListModel<String> historicoModel;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_BOTAO_AZUL = new Color(41, 128, 185);
    private final Color COR_BOTAO_VERDE = new Color(46, 204, 113);
    
    private List<AvaliacaoTemp> avaliacoes = new ArrayList<>();
    
    // Classe interna para armazenar avaliações temporárias
    private class AvaliacaoTemp {
        LocalDate data;
        double peso;
        double altura;
        double imc;
        double percentualGordura;
        double massaMuscular;
        String observacoes;
        
        AvaliacaoTemp(LocalDate data, double peso, double altura, double percentualGordura, double massaMuscular, String observacoes) {
            this.data = data;
            this.peso = peso;
            this.altura = altura;
            this.imc = peso / (altura * altura);
            this.percentualGordura = percentualGordura;
            this.massaMuscular = massaMuscular;
            this.observacoes = observacoes;
        }
        
        @Override
        public String toString() {
            return String.format("%s | Peso: %.1fkg | Altura: %.2fm | IMC: %.1f | Gordura: %.1f%% | Massa: %.1fkg",
                data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), peso, altura, imc, percentualGordura, massaMuscular);
        }
    }
    
    public PainelAvaliacaoFisica() {
        alunoController = new AlunoController();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("Avaliação Física", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COR_FUNDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Buscar Aluno
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblBusca = new JLabel("ID do Aluno:");
        lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblBusca, gbc);
        gbc.gridx = 1;
        txtBuscaAluno = new JTextField(10);
        mainPanel.add(txtBuscaAluno, gbc);
        gbc.gridx = 2;
        JButton btnBuscar = new JButton("Buscar Aluno");
        btnBuscar.setBackground(COR_BOTAO_AZUL);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> buscarAluno());
        mainPanel.add(btnBuscar, gbc);
        
        // Nome do Aluno
        gbc.gridx = 3; gbc.gridy = 0;
        JLabel lblNome = new JLabel("Aluno:");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblNome, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 2;
        lblAlunoNome = new JLabel("Nenhum aluno selecionado");
        lblAlunoNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAlunoNome.setForeground(Color.GRAY);
        mainPanel.add(lblAlunoNome, gbc);
        gbc.gridwidth = 1;
        
        // Separador
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 6;
        JSeparator separator = new JSeparator();
        mainPanel.add(separator, gbc);
        gbc.gridwidth = 1;
        
        // Dados da Avaliação
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPeso = new JLabel("Peso (kg):");
        lblPeso.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblPeso, gbc);
        gbc.gridx = 1;
        txtPeso = new JTextField(10);
        mainPanel.add(txtPeso, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        JLabel lblAltura = new JLabel("Altura (m):");
        lblAltura.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblAltura, gbc);
        gbc.gridx = 3;
        txtAltura = new JTextField(10);
        mainPanel.add(txtAltura, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblGordura = new JLabel("% Gordura:");
        lblGordura.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblGordura, gbc);
        gbc.gridx = 1;
        txtPercentualGordura = new JTextField(10);
        mainPanel.add(txtPercentualGordura, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        JLabel lblMassa = new JLabel("Massa Muscular (kg):");
        lblMassa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblMassa, gbc);
        gbc.gridx = 3;
        txtMassaMuscular = new JTextField(10);
        mainPanel.add(txtMassaMuscular, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblObs = new JLabel("Observações:");
        lblObs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblObs, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 4;
        txtObservacoes = new JTextArea(3, 30);
        txtObservacoes.setBorder(BorderFactory.createLineBorder(COR_BORDA));
        JScrollPane scrollObs = new JScrollPane(txtObservacoes);
        mainPanel.add(scrollObs, gbc);
        gbc.gridwidth = 1;
        
        // Botão Salvar
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.gridwidth = 3;
        JButton btnSalvar = new JButton("Salvar Avaliação");
        btnSalvar.setBackground(COR_BOTAO_VERDE);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvarAvaliacao());
        mainPanel.add(btnSalvar, gbc);
        gbc.gridwidth = 1;
        
        // Histórico de Avaliações
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 6;
        JLabel lblHistorico = new JLabel("Histórico de Avaliações:");
        lblHistorico.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(lblHistorico, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 6;
        historicoModel = new DefaultListModel<>();
        listaHistorico = new JList<>(historicoModel);
        listaHistorico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listaHistorico.setBorder(BorderFactory.createLineBorder(COR_BORDA));
        listaHistorico.setFixedCellHeight(30);
        JScrollPane scrollHistorico = new JScrollPane(listaHistorico);
        scrollHistorico.setPreferredSize(new Dimension(800, 150));
        mainPanel.add(scrollHistorico, gbc);
        
        JScrollPane scrollMain = new JScrollPane(mainPanel);
        scrollMain.setBorder(null);
        add(scrollMain, BorderLayout.CENTER);
    }
    
    private void buscarAluno() {
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
            
            lblAlunoNome.setText(aluno.getNomeCompleto() + " (ID: " + aluno.getId() + ")");
            lblAlunoNome.setForeground(new Color(46, 204, 113));
            
            // Carregar histórico existente (simulado por enquanto)
            carregarHistoricoAluno(aluno);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarHistoricoAluno(Aluno aluno) {
        // Por enquanto, histórico simulado
        // Em produção, viria do banco de dados
        historicoModel.clear();
        // Exemplo de histórico
        // historicoModel.addElement("10/06/2026 | Peso: 75.5kg | Altura: 1.75m | IMC: 24.6 | Gordura: 18% | Massa: 32.5kg");
    }
    
    private void salvarAvaliacao() {
        if (lblAlunoNome.getText().equals("Nenhum aluno selecionado") || lblAlunoNome.getForeground() == Color.RED) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno válido primeiro!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double peso = Double.parseDouble(txtPeso.getText().replace(",", "."));
            double altura = Double.parseDouble(txtAltura.getText().replace(",", "."));
            double percentualGordura = Double.parseDouble(txtPercentualGordura.getText().replace(",", "."));
            double massaMuscular = Double.parseDouble(txtMassaMuscular.getText().replace(",", "."));
            String observacoes = txtObservacoes.getText().trim();
            
            if (peso <= 0 || altura <= 0) {
                JOptionPane.showMessageDialog(this, "Peso e altura devem ser maiores que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double imc = peso / (altura * altura);
            String classificacaoImc;
            if (imc < 18.5) classificacaoImc = "Abaixo do peso";
            else if (imc < 25) classificacaoImc = "Peso normal";
            else if (imc < 30) classificacaoImc = "Sobrepeso";
            else if (imc < 35) classificacaoImc = "Obesidade grau I";
            else if (imc < 40) classificacaoImc = "Obesidade grau II";
            else classificacaoImc = "Obesidade grau III";
            
            AvaliacaoTemp avaliacao = new AvaliacaoTemp(
                LocalDate.now(), peso, altura, percentualGordura, massaMuscular, observacoes
            );
            avaliacoes.add(avaliacao);
            historicoModel.addElement(avaliacao.toString());
            
            String mensagem = "╔══════════════════════════════════════════════════════════╗\n" +
                             "║                 AVALIAÇÃO FÍSICA REGISTRADA               ║\n" +
                             "╠══════════════════════════════════════════════════════════╣\n" +
                             "║ Aluno: " + lblAlunoNome.getText() + "\n" +
                             "║ Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                             "║ Peso: " + String.format("%.1f", peso) + " kg\n" +
                             "║ Altura: " + String.format("%.2f", altura) + " m\n" +
                             "║ IMC: " + String.format("%.1f", imc) + " (" + classificacaoImc + ")\n" +
                             "║ % Gordura: " + String.format("%.1f", percentualGordura) + "%\n" +
                             "║ Massa Muscular: " + String.format("%.1f", massaMuscular) + " kg\n" +
                             "╚══════════════════════════════════════════════════════════╝";
            
            JOptionPane.showMessageDialog(this, mensagem, "Avaliação Registrada", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpar campos
            txtPeso.setText("");
            txtAltura.setText("");
            txtPercentualGordura.setText("");
            txtMassaMuscular.setText("");
            txtObservacoes.setText("");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos numéricos corretamente!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}