package view;

import controller.AlunoController;
import controller.MatriculaController;
import controller.PlanoController;
import model.Aluno;
import model.Matricula;
import model.Plano;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DialogMatricula extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private Aluno aluno;
    private Matricula matricula;
    private MatriculaController matriculaController;
    private PlanoController planoController;
    
    private JComboBox<Plano> cbPlano;
    private JLabel lblNumeroMatricula;
    private JLabel lblDataMatricula;
    private JLabel lblProximoVencimento;
    private JComboBox<String> cbSituacao;
    private JCheckBox chkRenovacaoAutomatica;
    private JLabel lblStatusAtual;
    
    private boolean isEdicao;
    
    public DialogMatricula(JFrame parent, Aluno aluno) {
        super(parent, "Matrícula - " + aluno.getNomeCompleto(), true);
        this.aluno = aluno;
        this.matriculaController = new MatriculaController();
        this.planoController = new PlanoController();
        
        try {
            this.matricula = matriculaController.buscarPorAluno(aluno);
            this.isEdicao = matricula != null;
        } catch (Exception e) {
            this.matricula = null;
            this.isEdicao = false;
        }
        
        initComponents();
        
        if (isEdicao) {
            carregarDados();
        }
        
        carregarPlanos();
        
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 242, 245));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel(isEdicao ? "DADOS DA MATRÍCULA" : "NOVA MATRÍCULA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Informação se não tem matrícula
        if (!isEdicao) {
            lblStatusAtual = new JLabel("⚠️ Aluno não possui matrícula ativa. Crie uma abaixo:");
            lblStatusAtual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblStatusAtual.setForeground(new Color(231, 76, 60));
            lblStatusAtual.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(lblStatusAtual);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        // Painel de informações
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Número da Matrícula
        gbc.gridy = 0;
        gbc.gridx = 0;
        JLabel lblNum = new JLabel("Nº Matrícula:");
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblNum, gbc);
        gbc.gridx = 1;
        lblNumeroMatricula = new JLabel(isEdicao ? matricula.getNumeroMatricula() : "Será gerado automaticamente");
        lblNumeroMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNumeroMatricula.setForeground(isEdicao ? new Color(46, 204, 113) : Color.GRAY);
        infoPanel.add(lblNumeroMatricula, gbc);
        
        // Data da Matrícula
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel lblData = new JLabel("Data Matrícula:");
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblData, gbc);
        gbc.gridx = 1;
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        lblDataMatricula = new JLabel(isEdicao ? matricula.getDataMatricula().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : dataAtual);
        lblDataMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(lblDataMatricula, gbc);
        
        // Plano
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel lblPlano = new JLabel("Plano:");
        lblPlano.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblPlano, gbc);
        gbc.gridx = 1;
        cbPlano = new JComboBox<>();
        cbPlano.setPreferredSize(new Dimension(250, 30));
        cbPlano.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Plano) {
                    Plano p = (Plano) value;
                    String duracao = "";
                    switch (p.getDuracao()) {
                        case MENSAL: duracao = "Mensal"; break;
                        case TRIMESTRAL: duracao = "Trimestral"; break;
                        case SEMESTRAL: duracao = "Semestral"; break;
                        case ANUAL: duracao = "Anual"; break;
                    }
                    value = p.getNome() + " - " + duracao + " - R$ " + String.format("%.2f", p.getValor());
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        cbPlano.addActionListener(e -> atualizarVencimento());
        infoPanel.add(cbPlano, gbc);
        
        // Próximo Vencimento
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel lblVencimento = new JLabel("Próximo Vencimento:");
        lblVencimento.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblVencimento, gbc);
        gbc.gridx = 1;
        lblProximoVencimento = new JLabel("");
        lblProximoVencimento.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProximoVencimento.setForeground(new Color(231, 76, 60));
        infoPanel.add(lblProximoVencimento, gbc);
        
        // Situação
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel lblSituacao = new JLabel("Situação:");
        lblSituacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblSituacao, gbc);
        gbc.gridx = 1;
        cbSituacao = new JComboBox<>(new String[]{"ATIVA", "VENCIDA", "CANCELADA"});
        cbSituacao.setPreferredSize(new Dimension(150, 30));
        infoPanel.add(cbSituacao, gbc);
        
        // Renovação Automática
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel lblRenovacao = new JLabel("Renovação Automática:");
        lblRenovacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblRenovacao, gbc);
        gbc.gridx = 1;
        chkRenovacaoAutomatica = new JCheckBox("Ativar renovação automática");
        chkRenovacaoAutomatica.setBackground(Color.WHITE);
        infoPanel.add(chkRenovacaoAutomatica, gbc);
        
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton btnSalvar = new JButton(isEdicao ? "Atualizar Matrícula" : "Salvar Matrícula");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvarMatricula());
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.setBackground(new Color(149, 165, 166));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFechar.setFocusPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnFechar);
        mainPanel.add(buttonPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Atualizar vencimento inicial
        atualizarVencimento();
    }
    
    private void carregarPlanos() {
        try {
            cbPlano.removeAllItems();
            List<Plano> planos = planoController.listarTodos();
            for (Plano p : planos) {
                cbPlano.addItem(p);
            }
            
            if (isEdicao && matricula.getPlano() != null) {
                for (int i = 0; i < cbPlano.getItemCount(); i++) {
                    if (cbPlano.getItemAt(i).getId() == matricula.getPlano().getId()) {
                        cbPlano.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            atualizarVencimento();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar planos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarVencimento() {
        Plano plano = (Plano) cbPlano.getSelectedItem();
        if (plano != null) {
            LocalDate vencimento = LocalDate.now().plusMonths(plano.getMesesDuracao());
            lblProximoVencimento.setText(vencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }
    
    private void carregarDados() {
        if (matricula != null) {
            lblNumeroMatricula.setText(matricula.getNumeroMatricula());
            lblDataMatricula.setText(matricula.getDataMatricula().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            lblProximoVencimento.setText(matricula.getProximoVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            cbSituacao.setSelectedItem(matricula.getSituacao().toString());
            chkRenovacaoAutomatica.setSelected(matricula.isRenovacaoAutomatica());
        }
    }
    
    private void salvarMatricula() {
        try {
            Plano plano = (Plano) cbPlano.getSelectedItem();
            if (plano == null) {
                JOptionPane.showMessageDialog(this, "Selecione um plano!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isEdicao) {
                // Atualizar matrícula existente
                matricula.setPlano(plano);
                matricula.setSituacao(Matricula.SituacaoMatricula.valueOf((String) cbSituacao.getSelectedItem()));
                matricula.setRenovacaoAutomatica(chkRenovacaoAutomatica.isSelected());
                matriculaController.atualizar(matricula);
                
                JOptionPane.showMessageDialog(this,
                    "Matrícula atualizada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Criar nova matrícula
                Matricula novaMatricula = new Matricula(aluno, plano, LocalDate.now());
                novaMatricula.setSituacao(Matricula.SituacaoMatricula.valueOf((String) cbSituacao.getSelectedItem()));
                novaMatricula.setRenovacaoAutomatica(chkRenovacaoAutomatica.isSelected());
                matriculaController.inserir(novaMatricula);
                
                JOptionPane.showMessageDialog(this,
                    "Matrícula criada com sucesso!\n\n" +
                    "Nº Matrícula: " + novaMatricula.getNumeroMatricula() + "\n" +
                    "Plano: " + plano.getNome() + "\n" +
                    "Vencimento: " + novaMatricula.getProximoVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar matrícula: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}