package view;

import controller.PlanoController;
import model.Plano;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelPlanos extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTable tabelaPlanos;
    private DefaultTableModel modeloTabela;
    private PlanoController planoController;
    
    private final Color COR_FUNDO = new Color(240, 242, 245);
    private final Color COR_PAINEL_BRANCO = Color.WHITE;
    private final Color COR_BORDA = new Color(180, 180, 180);
    private final Color COR_BOTAO_AZUL = new Color(41, 128, 185);
    private final Color COR_BOTAO_VERDE = new Color(46, 204, 113);
    private final Color COR_BOTAO_VERMELHO = new Color(231, 76, 60);
    private final Color COR_BOTAO_AMARELO = new Color(241, 196, 15);
    
    public PainelPlanos() {
        planoController = new PlanoController();
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
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(COR_PAINEL_BRANCO);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblTitulo = new JLabel("Gerenciamento de Planos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(44, 62, 80));
        
        JButton btnNovo = new JButton("+ Novo Plano");
        btnNovo.setBackground(COR_BOTAO_VERDE);
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNovo.setFocusPainted(false);
        btnNovo.setBorderPainted(false);
        btnNovo.setOpaque(true);
        btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNovo.addActionListener(e -> abrirFormularioPlano(null));
        
        toolbar.add(lblTitulo, BorderLayout.WEST);
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
        
        String[] colunas = {"ID", "Nome", "Valor", "Duração", "Benefícios", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaPlanos = new JTable(modeloTabela);
        
        // Cabeçalho da tabela
        tabelaPlanos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaPlanos.getTableHeader().setBackground(new Color(44, 62, 80));
        tabelaPlanos.getTableHeader().setForeground(Color.WHITE);
        tabelaPlanos.getTableHeader().setReorderingAllowed(false);
        
        tabelaPlanos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaPlanos.setRowHeight(35);
        tabelaPlanos.setForeground(new Color(51, 51, 51));
        tabelaPlanos.setBackground(Color.WHITE);
        tabelaPlanos.setSelectionBackground(COR_BOTAO_AZUL);
        tabelaPlanos.setSelectionForeground(Color.WHITE);
        tabelaPlanos.setGridColor(COR_BORDA);
        tabelaPlanos.setShowVerticalLines(true);
        
        JScrollPane scrollPane = new JScrollPane(tabelaPlanos);
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
        btnEditar.setOpaque(true);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.addActionListener(e -> editarPlano());
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(COR_BOTAO_VERMELHO);
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setOpaque(true);
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(e -> excluirPlano());
        
        panel.add(btnEditar);
        panel.add(btnExcluir);
        
        return panel;
    }
    
    private void carregarDados() {
        try {
            List<Plano> planos = planoController.listarTodos();
            atualizarTabela(planos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar planos: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<Plano> planos) {
        modeloTabela.setRowCount(0);
        
        for (Plano plano : planos) {
            String status = plano.isAtivo() ? "Ativo" : "Inativo";
            String duracao = "";
            switch (plano.getDuracao()) {
                case MENSAL: duracao = "Mensal"; break;
                case TRIMESTRAL: duracao = "Trimestral"; break;
                case SEMESTRAL: duracao = "Semestral"; break;
                case ANUAL: duracao = "Anual"; break;
            }
            
            modeloTabela.addRow(new Object[]{
                plano.getId(),
                plano.getNome(),
                String.format("R$ %.2f", plano.getValor()),
                duracao,
                plano.getBeneficios() != null ? plano.getBeneficios() : "",
                status
            });
        }
    }
    
    private void abrirFormularioPlano(Plano plano) {
        FormularioPlano dialog = new FormularioPlano((JFrame) SwingUtilities.getWindowAncestor(this), plano);
        dialog.setVisible(true);
        carregarDados();
    }
    
    private void editarPlano() {
        int selectedRow = tabelaPlanos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um plano para editar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        try {
            Plano plano = planoController.buscarPorId(id);
            abrirFormularioPlano(plano);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar plano: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirPlano() {
        int selectedRow = tabelaPlanos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um plano para excluir",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este plano?\nPlanos vinculados a matrículas não podem ser excluídos!",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabela.getValueAt(selectedRow, 0);
            try {
                planoController.excluir(id);
                carregarDados();
                JOptionPane.showMessageDialog(this,
                    "Plano excluído com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}