package dao;

import model.Personal;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonalDAO {
    
    public void inserir(Personal personal) throws SQLException {
        String sql = "INSERT INTO personais (nome, cpf, telefone, email, especialidade, horario_trabalho, ativo, data_contratacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, personal.getNome());
            stmt.setString(2, personal.getCpf());
            stmt.setString(3, personal.getTelefone());
            stmt.setString(4, personal.getEmail());
            stmt.setString(5, personal.getEspecialidade());
            stmt.setString(6, personal.getHorarioTrabalho());
            stmt.setBoolean(7, personal.isAtivo());
            stmt.setDate(8, Date.valueOf(personal.getDataContratacao()));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    personal.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public void atualizar(Personal personal) throws SQLException {
        String sql = "UPDATE personais SET nome = ?, cpf = ?, telefone = ?, email = ?, especialidade = ?, horario_trabalho = ?, ativo = ?, data_contratacao = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, personal.getNome());
            stmt.setString(2, personal.getCpf());
            stmt.setString(3, personal.getTelefone());
            stmt.setString(4, personal.getEmail());
            stmt.setString(5, personal.getEspecialidade());
            stmt.setString(6, personal.getHorarioTrabalho());
            stmt.setBoolean(7, personal.isAtivo());
            stmt.setDate(8, Date.valueOf(personal.getDataContratacao()));
            stmt.setInt(9, personal.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM personais WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public Personal buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM personais WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairPersonal(rs);
                }
            }
        }
        return null;
    }
    
    public List<Personal> listarTodos() throws SQLException {
        List<Personal> personais = new ArrayList<>();
        String sql = "SELECT * FROM personais ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                personais.add(extrairPersonal(rs));
            }
        }
        return personais;
    }
    
    public List<Personal> buscarPorNome(String nome) throws SQLException {
        List<Personal> personais = new ArrayList<>();
        String sql = "SELECT * FROM personais WHERE nome LIKE ? ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    personais.add(extrairPersonal(rs));
                }
            }
        }
        return personais;
    }
    
    private Personal extrairPersonal(ResultSet rs) throws SQLException {
        Personal personal = new Personal();
        personal.setId(rs.getInt("id"));
        personal.setNome(rs.getString("nome"));
        personal.setCpf(rs.getString("cpf"));
        personal.setTelefone(rs.getString("telefone"));
        personal.setEmail(rs.getString("email"));
        personal.setEspecialidade(rs.getString("especialidade"));
        personal.setHorarioTrabalho(rs.getString("horario_trabalho"));
        personal.setAtivo(rs.getBoolean("ativo"));
        personal.setDataContratacao(rs.getDate("data_contratacao").toLocalDate());
        return personal;
    }
}