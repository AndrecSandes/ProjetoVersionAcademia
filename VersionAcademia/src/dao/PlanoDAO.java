package dao;

import model.Plano;
import model.Plano.Duracao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanoDAO {
    
    public void inserir(Plano plano) throws SQLException {
        String sql = "INSERT INTO planos (nome, valor, duracao, beneficios, ativo)"
        		+ " VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, plano.getNome());
            stmt.setDouble(2, plano.getValor());
            stmt.setString(3, plano.getDuracao().name());
            stmt.setString(4, plano.getBeneficios());
            stmt.setBoolean(5, plano.isAtivo());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    plano.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public void atualizar(Plano plano) throws SQLException {
        String sql = "UPDATE planos SET nome = ?, valor = ?, duracao = ?, beneficios = ?, ativo = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plano.getNome());
            stmt.setDouble(2, plano.getValor());
            stmt.setString(3, plano.getDuracao().name());
            stmt.setString(4, plano.getBeneficios());
            stmt.setBoolean(5, plano.isAtivo());
            stmt.setInt(6, plano.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM planos WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public Plano buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM planos WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairPlano(rs);
                }
            }
        }
        return null;
    }
    
    public List<Plano> listarTodos() throws SQLException {
        List<Plano> planos = new ArrayList<>();
        String sql = "SELECT * FROM planos ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                planos.add(extrairPlano(rs));
            }
        }
        return planos;
    }
    
    private Plano extrairPlano(ResultSet rs) throws SQLException {
        Plano plano = new Plano();
        plano.setId(rs.getInt("id"));
        plano.setNome(rs.getString("nome"));
        plano.setValor(rs.getDouble("valor"));
        plano.setDuracao(Duracao.valueOf(rs.getString("duracao")));
        plano.setBeneficios(rs.getString("beneficios"));
        plano.setAtivo(rs.getBoolean("ativo"));
        return plano;
    }
}