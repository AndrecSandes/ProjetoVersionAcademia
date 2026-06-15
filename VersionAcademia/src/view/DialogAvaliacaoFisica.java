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

public class DialogAvaliacaoFisica extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private Aluno aluno;
    private JTextField txtPeso;
    private JTextField txtAltura;
    private JTextField txtPercentualGordura;
    private JTextField txtMassaMuscular;
    private JTextArea txtObservacoes;
    private JList<String> listaHistorico;
    private DefaultListModel<String> historicoModel;
    
    private List<AvaliacaoTemp> avaliacoes = new ArrayList<>();
    
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
    
    public DialogAvaliacaoFisica(JFrame parent, Aluno aluno) {
        super(parent, "Avaliação Física - " + aluno.getNomeCompleto(), true);
        this.aluno = aluno;
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 242, 245));
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Título do Aluno
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblAluno = new JLabel("Aluno: " + aluno.getNomeCompleto());
        lblAluno.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAluno.setForeground(new Color(44, 62, 80));
        mainPanel.add(lblAluno, gbc);
        gbc.gridwidth = 1;
        
        // Dados da Avaliação
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel lblPeso = new JLabel("Peso (kg):");
        lblPeso.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblPeso, gbc);
        gbc.gridx = 1;
        txtPeso = new JTextField(10);
        mainPanel.add(txtPeso, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel lblAltura = new JLabel("Altura (m):");
        lblAltura.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblAltura, gbc);
        gbc.gridx = 1;
        txtAltura = new JTextField(10);
        mainPanel.add(txtAltura, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel lblGordura = new JLabel("% Gordura:");
        lblGordura.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblGordura, gbc);
        gbc.gridx = 1;
        txtPercentualGordura = new JTextField(10);
        mainPanel.add(txtPercentualGordura, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel lblMassa = new JLabel("Massa Muscular (kg):");
        lblMassa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblMassa, gbc);
        gbc.gridx = 1;
        txtMassaMuscular = new JTextField(10);
        mainPanel.add(txtMassaMuscular, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel lblObs = new JLabel("Observações:");
        lblObs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblObs, gbc);
        gbc.gridx = 1;
        txtObservacoes = new JTextArea(3, 20);
        txtObservacoes.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        JScrollPane scrollObs = new JScrollPane(txtObservacoes);
        mainPanel.add(scrollObs, gbc);
        
        // Botão Salvar
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton btnSalvar = new JButton("Salvar Avaliação");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvarAvaliacao());
        mainPanel.add(btnSalvar, gbc);
        
        // Histórico
        gbc.gridy = 7;
        JLabel lblHistorico = new JLabel("Histórico de Avaliações:");
        lblHistorico.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(lblHistorico, gbc);
        
        gbc.gridy = 8;
        historicoModel = new DefaultListModel<>();
        listaHistorico = new JList<>(historicoModel);
        listaHistorico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listaHistorico.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        JScrollPane scrollHistorico = new JScrollPane(listaHistorico);
        scrollHistorico.setPreferredSize(new Dimension(600, 150));
        mainPanel.add(scrollHistorico, gbc);
        
        // Botão Fechar
        gbc.gridy = 9;
        JButton btnFechar = new JButton("Fechar");
        btnFechar.setBackground(new Color(149, 165, 166));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFechar.setFocusPainted(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());
        mainPanel.add(btnFechar, gbc);
        
        JScrollPane scrollMain = new JScrollPane(mainPanel);
        scrollMain.setBorder(null);
        add(scrollMain, BorderLayout.CENTER);
    }
    
    private void salvarAvaliacao() {
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
                             "║ Aluno: " + aluno.getNomeCompleto() + "\n" +
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