package view;

import controller.PersonalController;
import model.Personal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelPersonais extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTable tabelaPersonais;
    private DefaultTableModel modeloTabela;
    private JTextField txtBusca;
    private PersonalController personalController;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_BOTAO_AZUL = new Color(41, 128, 185);
    private final Color COR_BOTAO_VERDE = new Color(46, 204, 113);
    private final Color COR_BOTAO_VERMELHO = new Color(231, 76, 60);
    private final Color COR_BOTAO_AMARELO = new Color(241, 196, 15);
    
    public PainelPersonais() {
        personalController = new PersonalController();
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        JPanel toolbar = criarToolbar();
        add(toolbar, BorderLayout.NORTH);
        
        JPanel tablePanel = criarPainelTabela();
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel botoesAcao = criarBotoesAcao();
        add(botoesAcao, BorderLayout.SOUTH);
    }
    
    private JPanel criarToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(15, 0));
        toolbar.setBackground(COR_PAINEL_BRANCO);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel pesquisaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pesquisaPanel.setBackground(COR_PAINEL_BRANCO);
        
        JLabel lblBusca = new JLabel("Buscar:");
        lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtBusca = new JTextField(20);
        txtBusca.setToolTipText("Digite o nome do personal");
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBusca.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setBackground(COR_BOTAO_AZUL);
        btnPesquisar.setForeground(Color.WHITE);
        btnPesquisar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPesquisar.setFocusPainted(false);
        btnPesquisar.setBorderPainted(false);
        btnPesquisar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPesquisar.addActionListener(e -> pesquisarPersonais());
        
        pesquisaPanel.add(lblBusca);
        pesquisaPanel.add(txtBusca);
        pesquisaPanel.add(btnPesquisar);
        
        JButton btnNovo = new JButton("+ Novo Personal");
        btnNovo.setBackground(COR_BOTAO_VERDE);
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNovo.setFocusPainted(false);
        btnNovo.setBorderPainted(false);
        btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNovo.addActionListener(e -> abrirFormularioPersonal(null));
        
        toolbar.add(pesquisaPanel, BorderLayout.WEST);
        toolbar.add(btnNovo, BorderLayout.EAST);
        
        return toolbar;
    }
    
    private JPanel criarPainelTabela() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Especialidade", "Data Contratação", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaPersonais = new JTable(modeloTabela);
        
        tabelaPersonais.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaPersonais.getTableHeader().setBackground(new Color(44, 62, 80));
        tabelaPersonais.getTableHeader().setForeground(Color.WHITE);
        tabelaPersonais.getTableHeader().setReorderingAllowed(false);
        
        tabelaPersonais.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaPersonais.setRowHeight(35);
        tabelaPersonais.setForeground(new Color(51, 51, 51));
        tabelaPersonais.setBackground(Color.WHITE);
        tabelaPersonais.setSelectionBackground(COR_BOTAO_AZUL);
        tabelaPersonais.setSelectionForeground(Color.WHITE);
        tabelaPersonais.setGridColor(COR_BORDA);
        tabelaPersonais.setShowVerticalLines(true);
        
        JScrollPane scrollPane = new JScrollPane(tabelaPersonais);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarBotoesAcao() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(COR_PAINEL_BRANCO);
        panel.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(COR_BOTAO_AMARELO);
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditar.setFocusPainted(false);
        btnEditar.setBorderPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.addActionListener(e -> editarPersonal());
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(COR_BOTAO_VERMELHO);
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(e -> excluirPersonal());
        
        panel.add(btnEditar);
        panel.add(btnExcluir);
        
        return panel;
    }
    
    private void carregarDados() {
        try {
            List<Personal> personais = personalController.listarTodos();
            atualizarTabela(personais);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar personais: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<Personal> personais) {
        modeloTabela.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Personal p : personais) {
            String status = p.isAtivo() ? "Ativo" : "Inativo";
            
            modeloTabela.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getCpf(),
                p.getTelefone() != null ? p.getTelefone() : "",
                p.getEspecialidade() != null ? p.getEspecialidade() : "",
                p.getDataContratacao().format(formatter),
                status
            });
        }
    }
    
    private void pesquisarPersonais() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            carregarDados();
            return;
        }
        
        try {
            List<Personal> personais = personalController.buscarPorNome(termo);
            atualizarTabela(personais);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro na pesquisa: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirFormularioPersonal(Personal personal) {
        FormularioPersonal dialog = new FormularioPersonal((JFrame) SwingUtilities.getWindowAncestor(this), personal);
        dialog.setVisible(true);
        carregarDados();
    }
    
    private void editarPersonal() {
        int selectedRow = tabelaPersonais.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um personal para editar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        try {
            Personal personal = personalController.buscarPorId(id);
            abrirFormularioPersonal(personal);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar personal: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirPersonal() {
        int selectedRow = tabelaPersonais.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um personal para excluir",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este personal?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabela.getValueAt(selectedRow, 0);
            try {
                personalController.excluir(id);
                carregarDados();
                JOptionPane.showMessageDialog(this,
                    "Personal excluído com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}