package view;

import controller.AlunoController;
import model.Aluno;
import model.Aluno.Sexo;
import model.Aluno.StatusAluno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormularioAluno extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNome;
    private JFormattedTextField txtCPF;
    private JFormattedTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtEndereco;
    private JTextField txtContatoEmergencia;
    private JComboBox<Sexo> cbSexo;
    private JComboBox<StatusAluno> cbStatus;
    private JFormattedTextField ftfDataNascimento, ftfDataMatricula;
    private Aluno aluno;
    private AlunoController controller;
    private boolean isEdicao;
    
    private LocalDate converterData(String texto) {
        if (texto == null || texto.trim().isEmpty() || texto.contains("_")) {
            return null;
        }
        try {
            return LocalDate.parse(texto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return null;
        }
    }
    
    public FormularioAluno(JFrame parent, Aluno aluno) {
        super(parent, aluno == null ? "Novo Aluno" : "Editar Aluno", true);
        this.aluno = aluno;
        this.isEdicao = aluno != null;
        this.controller = new AlunoController();
        
        initComponents();
        if (isEdicao) {
            carregarDados();
        }
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 242, 245));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 242, 245));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        try {
            MaskFormatter mascaraCPF = new MaskFormatter("###.###.###-##");
            MaskFormatter mascaraTelefone = new MaskFormatter("(##) #####-####");
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            
            txtCPF = new JFormattedTextField(mascaraCPF);
            txtTelefone = new JFormattedTextField(mascaraTelefone);
            ftfDataNascimento = new JFormattedTextField(mascaraData);
            ftfDataMatricula = new JFormattedTextField(mascaraData);
        } catch (java.text.ParseException e) {
            txtCPF = new JFormattedTextField();
            txtTelefone = new JFormattedTextField();
            ftfDataNascimento = new JFormattedTextField();
            ftfDataMatricula = new JFormattedTextField();
        }
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblNome, gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(25);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtNome, gbc);
        
        // CPF
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblCPF, gbc);
        gbc.gridx = 1;
        txtCPF.setColumns(14);
        txtCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtCPF, gbc);
        
        // Data Nascimento
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDataNasc = new JLabel("Data Nascimento:");
        lblDataNasc.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblDataNasc, gbc);
        gbc.gridx = 1;
        ftfDataNascimento.setColumns(10);
        ftfDataNascimento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(ftfDataNascimento, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblTelefone, gbc);
        gbc.gridx = 1;
        txtTelefone.setColumns(15);
        txtTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtTelefone, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(30);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtEmail, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblEndereco, gbc);
        gbc.gridx = 1;
        txtEndereco = new JTextField(40);
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtEndereco, gbc);
        
        // Sexo
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblSexo, gbc);
        gbc.gridx = 1;
        cbSexo = new JComboBox<>(Sexo.values());
        cbSexo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(cbSexo, gbc);
        
        // Contato Emergência
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel lblContatoEmerg = new JLabel("Contato Emergência:");
        lblContatoEmerg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblContatoEmerg, gbc);
        gbc.gridx = 1;
        txtContatoEmergencia = new JTextField(15);
        txtContatoEmergencia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtContatoEmergencia, gbc);
        
        // Data Matrícula
        gbc.gridx = 0; gbc.gridy = 8;
        JLabel lblDataMat = new JLabel("Data Matrícula:");
        lblDataMat.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblDataMat, gbc);
        gbc.gridx = 1;
        ftfDataMatricula.setColumns(10);
        ftfDataMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ftfDataMatricula.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        formPanel.add(ftfDataMatricula, gbc);
        
        // Status - Importante para edição
        gbc.gridx = 0; gbc.gridy = 9;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblStatus, gbc);
        gbc.gridx = 1;
        cbStatus = new JComboBox<>(StatusAluno.values());
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(cbStatus, gbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setOpaque(true);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvar());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(149, 165, 166));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setOpaque(true);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        
        btnSalvar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void carregarDados() {
        txtNome.setText(aluno.getNomeCompleto());
        txtCPF.setText(aluno.getCpf());
        
        if (aluno.getDataNascimento() != null) {
            ftfDataNascimento.setText(aluno.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        
        txtTelefone.setText(aluno.getTelefone());
        txtEmail.setText(aluno.getEmail());
        txtEndereco.setText(aluno.getEndereco());
        cbSexo.setSelectedItem(aluno.getSexo());
        txtContatoEmergencia.setText(aluno.getContatoEmergencia());
        
        if (aluno.getDataMatricula() != null) {
            ftfDataMatricula.setText(aluno.getDataMatricula().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        
        cbStatus.setSelectedItem(aluno.getStatus());
    }
    
    private void salvar() {
        try {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String cpf = txtCPF.getText().replaceAll("[^0-9]", "");
            if (cpf.length() != 11) {
                JOptionPane.showMessageDialog(this, "CPF inválido! Digite um CPF com 11 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isEdicao) {
                aluno = new Aluno();
            }
            
            aluno.setNomeCompleto(txtNome.getText().trim());
            aluno.setCpf(txtCPF.getText().trim());
            aluno.setDataNascimento(converterData(ftfDataNascimento.getText()));
            aluno.setTelefone(txtTelefone.getText().trim());
            aluno.setEmail(txtEmail.getText().trim());
            aluno.setEndereco(txtEndereco.getText().trim());
            aluno.setSexo((Sexo) cbSexo.getSelectedItem());
            aluno.setContatoEmergencia(txtContatoEmergencia.getText().trim());
            aluno.setDataMatricula(converterData(ftfDataMatricula.getText()));
            aluno.setStatus((StatusAluno) cbStatus.getSelectedItem());
            aluno.setAtivo(true);
            
            if (isEdicao) {
                controller.atualizar(aluno);
                JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                controller.salvar(aluno);
                JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}