package view;

import dao.ConexaoBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginView extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnSair;
    private JLabel lblStatus;
    
    private final Color COR_PRIMARIA = new Color(41, 128, 185);
    private final Color COR_BOTAO_VERDE = new Color(46, 204, 113);
    private final Color COR_BOTAO_VERMELHO = new Color(231, 76, 60);
    
    public LoginView() {
        initComponents();
        configurarJanela();
    }
    
    private void initComponents() {
        setTitle("Login - AcademyManager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Painel principal com fundo gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(44, 62, 80), getWidth(), getHeight(), new Color(22, 160, 133));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Card de login - centralizado
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(45, 50, 45, 50)
        ));
        
        // Adicionar sombra ao card
        JPanel shadowPanel = new JPanel(new BorderLayout());
        shadowPanel.setOpaque(false);
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        shadowPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Logo / Título
        JPanel tituloPanel = new JPanel();
        tituloPanel.setOpaque(false);
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Letra "A" estilizada
        JLabel lblIcon = new JLabel("A");
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 50));
        lblIcon.setForeground(COR_PRIMARIA);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloPanel.add(lblIcon);
        tituloPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel lblTitulo = new JLabel("AcademyManager");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloPanel.add(lblTitulo);
        
        JLabel lblSubtitulo = new JLabel("Sistema de Gestao de Academia");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloPanel.add(lblSubtitulo);
        
        cardPanel.add(tituloPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Separador
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        separator.setMaximumSize(new Dimension(300, 2));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(separator);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Campos de entrada
        JPanel camposPanel = new JPanel();
        camposPanel.setOpaque(false);
        camposPanel.setLayout(new BoxLayout(camposPanel, BoxLayout.Y_AXIS));
        camposPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposPanel.setMaximumSize(new Dimension(300, 200));
        
        // Usuário
        JLabel lblUsuario = new JLabel("USUARIO");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUsuario.setForeground(new Color(100, 100, 100));
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        camposPanel.add(lblUsuario);
        camposPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setMaximumSize(new Dimension(300, 40));
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        txtUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtSenha.requestFocus();
                }
            }
        });
        camposPanel.add(txtUsuario);
        camposPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Senha
        JLabel lblSenha = new JLabel("SENHA");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSenha.setForeground(new Color(100, 100, 100));
        lblSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        camposPanel.add(lblSenha);
        camposPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setMaximumSize(new Dimension(300, 40));
        txtSenha.setPreferredSize(new Dimension(300, 40));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        txtSenha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
        });
        camposPanel.add(txtSenha);
        
        cardPanel.add(camposPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Status
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(COR_BOTAO_VERMELHO);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblStatus);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.setOpaque(false);
        botoesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botoesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(COR_BOTAO_VERDE);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setPreferredSize(new Dimension(120, 40));
        btnEntrar.addActionListener(e -> realizarLogin());
        botoesPanel.add(btnEntrar);
        
        btnSair = new JButton("Sair");
        btnSair.setBackground(COR_BOTAO_VERMELHO);
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSair.setFocusPainted(false);
        btnSair.setBorderPainted(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setPreferredSize(new Dimension(120, 40));
        btnSair.addActionListener(e -> System.exit(0));
        botoesPanel.add(btnSair);
        
        cardPanel.add(botoesPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Versão
        JLabel lblVersao = new JLabel("versao 1.0");
        lblVersao.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVersao.setForeground(Color.GRAY);
        lblVersao.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblVersao);
        
        // Adicionar ao painel principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(shadowPanel, gbc);
        
        add(mainPanel);
        
        SwingUtilities.invokeLater(() -> txtUsuario.requestFocus());
    }
    
    private void configurarJanela() {
        setSize(450, 580);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            lblStatus.setText("Preencha todos os campos!");
            return;
        }
        
        if (usuario.equals("admin") && senha.equals("123")) {
            lblStatus.setText(" ");
            abrirSistema();
            return;
        }
        
        try {
            Connection conn = ConexaoBD.getConexao();
            String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ? AND ativo = 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                lblStatus.setText(" ");
                rs.close();
                stmt.close();
                conn.close();
                abrirSistema();
            } else {
                lblStatus.setText("Usuario ou senha invalidos!");
                txtSenha.setText("");
                txtSenha.requestFocus();
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            lblStatus.setText("Erro de conexao: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void abrirSistema() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}