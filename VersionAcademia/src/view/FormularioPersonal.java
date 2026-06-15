package view;

import controller.PersonalController;
import model.Personal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormularioPersonal extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNome;
    private JFormattedTextField ftfCPF;
    private JFormattedTextField ftfTelefone;
    private JTextField txtEmail;
    private JTextField txtEspecialidade;
    private JTextArea txtHorarioTrabalho;
    private JFormattedTextField ftfDataContratacao;
    private JCheckBox chkAtivo;
    
    private Personal personal;
    private PersonalController controller;
    private boolean isEdicao;
    
    public FormularioPersonal(JFrame parent, Personal personal) {
        super(parent, personal == null ? "Novo Personal" : "Editar Personal", true);
        this.personal = personal;
        this.isEdicao = personal != null;
        this.controller = new PersonalController();
        
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
        ftfCPF = new JFormattedTextField();
        ftfCPF.setColumns(14);
        ftfCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(ftfCPF, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblTelefone, gbc);
        gbc.gridx = 1;
        ftfTelefone = new JFormattedTextField();
        ftfTelefone.setColumns(15);
        ftfTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(ftfTelefone, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(30);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtEmail, gbc);
        
        // Especialidade
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblEspecialidade = new JLabel("Especialidade:");
        lblEspecialidade.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblEspecialidade, gbc);
        gbc.gridx = 1;
        txtEspecialidade = new JTextField(25);
        txtEspecialidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtEspecialidade, gbc);
        
        // Horário de Trabalho
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblHorario = new JLabel("Horário de Trabalho:");
        lblHorario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblHorario, gbc);
        gbc.gridx = 1;
        txtHorarioTrabalho = new JTextArea(3, 25);
        txtHorarioTrabalho.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtHorarioTrabalho.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        JScrollPane scrollHorario = new JScrollPane(txtHorarioTrabalho);
        formPanel.add(scrollHorario, gbc);
        
        // Data Contratação
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblDataContratacao = new JLabel("Data Contratação:");
        lblDataContratacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblDataContratacao, gbc);
        gbc.gridx = 1;
        ftfDataContratacao = new JFormattedTextField();
        ftfDataContratacao.setColumns(10);
        ftfDataContratacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ftfDataContratacao.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        formPanel.add(ftfDataContratacao, gbc);
        
        // Ativo
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel lblAtivo = new JLabel("Status:");
        lblAtivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblAtivo, gbc);
        gbc.gridx = 1;
        chkAtivo = new JCheckBox("Ativo");
        chkAtivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkAtivo.setBackground(new Color(240, 242, 245));
        chkAtivo.setSelected(true);
        formPanel.add(chkAtivo, gbc);
        
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
        txtNome.setText(personal.getNome());
        ftfCPF.setText(personal.getCpf());
        ftfTelefone.setText(personal.getTelefone());
        txtEmail.setText(personal.getEmail());
        txtEspecialidade.setText(personal.getEspecialidade());
        txtHorarioTrabalho.setText(personal.getHorarioTrabalho());
        ftfDataContratacao.setText(personal.getDataContratacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        chkAtivo.setSelected(personal.isAtivo());
    }
    
    private void salvar() {
        try {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String cpf = ftfCPF.getText().replaceAll("[^0-9]", "");
            if (cpf.length() != 11) {
                JOptionPane.showMessageDialog(this, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isEdicao) {
                personal = new Personal();
            }
            
            personal.setNome(txtNome.getText().trim());
            personal.setCpf(ftfCPF.getText().trim());
            personal.setTelefone(ftfTelefone.getText().trim());
            personal.setEmail(txtEmail.getText().trim());
            personal.setEspecialidade(txtEspecialidade.getText().trim());
            personal.setHorarioTrabalho(txtHorarioTrabalho.getText().trim());
            personal.setDataContratacao(LocalDate.parse(ftfDataContratacao.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            personal.setAtivo(chkAtivo.isSelected());
            
            if (isEdicao) {
                controller.atualizar(personal);
                JOptionPane.showMessageDialog(this, "Personal atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                controller.salvar(personal);
                JOptionPane.showMessageDialog(this, "Personal cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}