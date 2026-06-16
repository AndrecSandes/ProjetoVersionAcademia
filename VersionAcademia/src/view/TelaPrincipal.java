package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaPrincipal extends JFrame {
    private JPanel menuLateral;
    private JPanel areaConteudo;
    private CardLayout cardLayout;
    private JLabel lblUsuario;
    
    // Cores profissionais
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_MENU = new Color(44, 62, 80);
    private final Color COR_MENU_HOVER = new Color(52, 73, 94);
    private final Color COR_TEXTO_MENU = new Color(236, 240, 241);
    private final Color COR_TEXTO_SUBMENU = new Color(189, 195, 199);
    private final Color COR_BARRA_SUPERIOR = new Color(44, 62, 80);
    private final Color COR_BOTAO_SAIR = new Color(231, 76, 60);
    
    public TelaPrincipal() {
        initComponents();
        configurarJanela();
        criarMenuLateral();
        criarAreaConteudo();
    }
    
    private void initComponents() {
        setTitle("AcademyManager - Sistema de Gestão de Academia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
    }
    
    private void configurarJanela() {
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());
        
        JPanel barraSuperior = criarBarraSuperior();
        add(barraSuperior, BorderLayout.NORTH);
    }
    
    private JPanel criarBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COR_BARRA_SUPERIOR);
        barra.setBorder(new EmptyBorder(15, 20, 15, 20));
        barra.setPreferredSize(new Dimension(0, 70));
        
        JLabel lblTitulo = new JLabel(" AcademyManager");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        
        JPanel infoUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        infoUsuario.setOpaque(false);
        
        lblUsuario = new JLabel(" Admin | Administrador");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(COR_TEXTO_MENU);
        
        JButton btnSair = new JButton(" Sair");
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSair.setBackground(COR_BOTAO_SAIR);
        btnSair.setForeground(Color.WHITE);
        btnSair.setOpaque(true);
        btnSair.setContentAreaFilled(true);
        btnSair.setBorderPainted(false);
        btnSair.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnSair.setFocusPainted(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.addActionListener(e -> sairSistema());
        
        infoUsuario.add(lblUsuario);
        infoUsuario.add(btnSair);
        
        barra.add(lblTitulo, BorderLayout.WEST);
        barra.add(infoUsuario, BorderLayout.EAST);
        
        return barra;
    }
    
    private void criarMenuLateral() {
        menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(COR_MENU);
        menuLateral.setPreferredSize(new Dimension(250, 0));
        menuLateral.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Logo/Título no menu
        JLabel lblLogo = new JLabel("MENU PRINCIPAL");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLogo.setForeground(COR_TEXTO_MENU);
        lblLogo.setBorder(new EmptyBorder(10, 20, 20, 20));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLateral.add(lblLogo);
        
        // Itens do menu com ícones
        adicionarItemMenu(" Dashboard", new String[]{" Visão Geral", " Estatísticas"});
        adicionarItemMenu(" Alunos", new String[]{" Gerenciar Alunos"});
        adicionarItemMenu(" Planos", new String[]{" Gerenciar Planos"});
        adicionarItemMenu(" Financeiro", new String[]{" Controle Financeiro", " Pagamentos", " Mensalidades"});
        adicionarItemMenu(" Personais", new String[]{" Gerenciar Personais"});
        adicionarItemMenu(" Relatórios", new String[]{" Visualizar Relatórios"});
        adicionarItemMenu(" Sobre", new String[]{" Sobre o Sistema"});
        
        JScrollPane scrollMenu = new JScrollPane(menuLateral);
        scrollMenu.setBorder(null);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16);
        scrollMenu.setBackground(COR_MENU);
        add(scrollMenu, BorderLayout.WEST);
    }
    
    private void adicionarItemMenu(String titulo, String[] subItens) {
        JPanel itemContainer = new JPanel();
        itemContainer.setLayout(new BoxLayout(itemContainer, BoxLayout.Y_AXIS));
        itemContainer.setBackground(COR_MENU);
        itemContainer.setMaximumSize(new Dimension(250, 1000));
        
        // Item principal
        JPanel itemPrincipal = new JPanel(new BorderLayout());
        itemPrincipal.setBackground(COR_MENU);
        itemPrincipal.setBorder(new EmptyBorder(12, 20, 12, 20));
        itemPrincipal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(COR_TEXTO_MENU);
        
        JLabel lblSeta = new JLabel(subItens.length > 0 ? " ▼" : "");
        lblSeta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSeta.setForeground(COR_TEXTO_SUBMENU);
        
        itemPrincipal.add(lblTitulo, BorderLayout.WEST);
        itemPrincipal.add(lblSeta, BorderLayout.EAST);
        
        // Submenu
        JPanel subMenu = new JPanel();
        subMenu.setLayout(new BoxLayout(subMenu, BoxLayout.Y_AXIS));
        subMenu.setBackground(new Color(36, 54, 70));
        subMenu.setVisible(false);
        
        for (String subItem : subItens) {
            JPanel subItemPanel = new JPanel(new BorderLayout());
            subItemPanel.setBackground(new Color(36, 54, 70));
            subItemPanel.setBorder(new EmptyBorder(10, 35, 10, 20));
            subItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel lblSubItem = new JLabel(subItem);
            lblSubItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblSubItem.setForeground(COR_TEXTO_SUBMENU);
            
            subItemPanel.add(lblSubItem, BorderLayout.WEST);
            
            final String telaNome = subItem.trim();
            subItemPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    trocarTela(telaNome);
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    subItemPanel.setBackground(COR_MENU_HOVER);
                    lblSubItem.setForeground(Color.WHITE);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    subItemPanel.setBackground(new Color(36, 54, 70));
                    lblSubItem.setForeground(COR_TEXTO_SUBMENU);
                }
            });
            
            subMenu.add(subItemPanel);
        }
        
        itemPrincipal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                subMenu.setVisible(!subMenu.isVisible());
                lblSeta.setText(subMenu.isVisible() ? " ▲" : " ▼");
                if (subItens.length == 0) {
                    trocarTela(titulo.trim());
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPrincipal.setBackground(COR_MENU_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                itemPrincipal.setBackground(COR_MENU);
            }
        });
        
        itemContainer.add(itemPrincipal);
        itemContainer.add(subMenu);
        menuLateral.add(itemContainer);
        menuLateral.add(Box.createRigidArea(new Dimension(0, 2)));
    }
    
    private void criarAreaConteudo() {
        areaConteudo = new JPanel();
        cardLayout = new CardLayout();
        areaConteudo.setLayout(cardLayout);
        areaConteudo.setBackground(COR_FUNDO);
        areaConteudo.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Criar os painéis
        PainelDashboard dashboard = new PainelDashboard();
        PainelEstatisticas estatisticas = new PainelEstatisticas();
        
        // Registrar telas
        areaConteudo.add(dashboard, "Visão Geral");
        areaConteudo.add(new PainelAlunos(dashboard), "Gerenciar Alunos");
        areaConteudo.add(new PainelPlanos(), "Gerenciar Planos");
        areaConteudo.add(new PainelFinanceiro(), "Controle Financeiro");
        areaConteudo.add(new PainelPagamentos(), "Pagamentos");
        areaConteudo.add(new PainelRelatorios(), "Visualizar Relatórios");
        areaConteudo.add(estatisticas, "Estatísticas");
        areaConteudo.add(criarPainelPersonais(), "Gerenciar Personais");
        areaConteudo.add(criarPainelSobre(), "Sobre o Sistema");
        
        add(areaConteudo, BorderLayout.CENTER);
        
        // Tela inicial
        cardLayout.show(areaConteudo, "Visão Geral");
    }
    
    private void trocarTela(String nomeTela) {
        cardLayout.show(areaConteudo, nomeTela);
    }
    
    private JPanel criarPainelSobre() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));
        
        JTextArea texto = new JTextArea();
        texto.setText("ACADEMY MANAGER v1.0\n\n" +
                     "Sistema completo para gestão de academias\n\n" +
                     "Desenvolvido com: Java 17, Swing, MySQL\n\n" +
                     "Módulos:\n" +
                     " • Alunos - Cadastro, edição e controle de alunos\n" +
                     " • Planos - Gerenciamento de planos e valores\n" +
                     " • Financeiro - Controle de pagamentos e receitas\n" +
                     " • Avaliações - Acompanhamento de evolução física\n" +
                     " • Relatórios - Estatísticas e relatórios gerenciais\n\n" +
                     "© 2024 - Todos os direitos reservados");
        texto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        texto.setForeground(new Color(44, 62, 80));
        texto.setBackground(Color.WHITE);
        texto.setEditable(false);
        
        card.add(texto, BorderLayout.CENTER);
        painel.add(card, BorderLayout.CENTER);
        return painel;
    }
    
    private JPanel criarPainelEstatisticas() {
        return new PainelEstatisticas();
    }
    
    private JPanel criarCardEstatistica(String titulo, String valor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(44, 62, 80));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        infoPanel.setOpaque(false);
        infoPanel.add(lblTitulo);
        infoPanel.add(lblValor);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarPainelFinanceiroMes() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));
        
        JLabel label = new JLabel("Financeiro do Mês", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(44, 62, 80));
        
        card.add(label, BorderLayout.CENTER);
        painel.add(card, BorderLayout.CENTER);
        return painel;
    }
    
    private JPanel criarPainelPagamentos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));
        
        JLabel label = new JLabel("Controle de Pagamentos", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(44, 62, 80));
        
        card.add(label, BorderLayout.CENTER);
        painel.add(card, BorderLayout.CENTER);
        return painel;
    }
    
    private JPanel criarPainelMensalidades() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));
        
        JLabel label = new JLabel("Controle de Mensalidades", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(44, 62, 80));
        
        card.add(label, BorderLayout.CENTER);
        painel.add(card, BorderLayout.CENTER);
        return painel;
    }
    
    private JPanel criarPainelPersonais() {
    	return new PainelPersonais();
    }
    
    private JPanel criarPainelRelatorios() {
    	return new PainelRelatorios();
    }
    
    private void sairSistema() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}