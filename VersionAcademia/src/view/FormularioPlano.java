package view;

import controller.PlanoController;
import model.Plano;
import model.Plano.Duracao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioPlano extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNome;
    private JFormattedTextField ftfValor;
    private JComboBox<Duracao> cbDuracao;
    private JTextArea txtBeneficios;
    private JCheckBox chkAtivo;
    
    private Plano plano;
    private PlanoController controller;
    private boolean isEdicao;
    
    public FormularioPlano(JFrame parent, Plano plano) {
        super(parent, plano == null ? "Novo Plano" : "Editar Plano", true);
        this.plano = plano;
        this.isEdicao = plano != null;
        this.controller = new PlanoController();
        
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
        
        // Nome do Plano
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNome = new JLabel("Nome do Plano:");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblNome, gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(25);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(txtNome, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblValor, gbc);
        gbc.gridx = 1;
        ftfValor = new JFormattedTextField();
        ftfValor.setColumns(10);
        ftfValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(ftfValor, gbc);
        
        // Duração
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDuracao = new JLabel("Duração:");
        lblDuracao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblDuracao, gbc);
        gbc.gridx = 1;
        cbDuracao = new JComboBox<>(Duracao.values());
        cbDuracao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(cbDuracao, gbc);
        
        // Benefícios
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblBeneficios = new JLabel("Benefícios:");
        lblBeneficios.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblBeneficios, gbc);
        gbc.gridx = 1;
        txtBeneficios = new JTextArea(5, 25);
        txtBeneficios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBeneficios.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        JScrollPane scrollBeneficios = new JScrollPane(txtBeneficios);
        formPanel.add(scrollBeneficios, gbc);
        
        // Ativo
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblAtivo = new JLabel("Status:");
        lblAtivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblAtivo, gbc);
        gbc.gridx = 1;
        chkAtivo = new JCheckBox("Plano Ativo");
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
        txtNome.setText(plano.getNome());
        ftfValor.setText(String.valueOf(plano.getValor()));
        cbDuracao.setSelectedItem(plano.getDuracao());
        txtBeneficios.setText(plano.getBeneficios());
        chkAtivo.setSelected(plano.isAtivo());
    }
    
    private void salvar() {
        try {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome do plano é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double valor;
            try {
                valor = Double.parseDouble(ftfValor.getText().replace(",", "."));
                if (valor <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido! Digite um número positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isEdicao) {
                plano = new Plano();
            }
            
            plano.setNome(txtNome.getText().trim());
            plano.setValor(valor);
            plano.setDuracao((Duracao) cbDuracao.getSelectedItem());
            plano.setBeneficios(txtBeneficios.getText().trim());
            plano.setAtivo(chkAtivo.isSelected());
            
            if (isEdicao) {
                controller.atualizar(plano);
                JOptionPane.showMessageDialog(this, "Plano atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                controller.salvar(plano);
                JOptionPane.showMessageDialog(this, "Plano cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}