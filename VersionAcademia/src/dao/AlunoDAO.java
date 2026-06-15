package dao;

import model.Aluno;
import model.Aluno.StatusAluno;
import model.Aluno.Sexo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    
    public void inserir(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO alunos (nome_completo, cpf, data_nascimento, telefone, email, "
        		+ "endereco, sexo, contato_emergencia, data_matricula, status, ativo) "
        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, aluno.getNomeCompleto());
            stmt.setString(2, aluno.getCpf());
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            stmt.setString(4, aluno.getTelefone());
            stmt.setString(5, aluno.getEmail());
            stmt.setString(6, aluno.getEndereco());
            stmt.setString(7, aluno.getSexo().name());
            stmt.setString(8, aluno.getContatoEmergencia());
            stmt.setDate(9, Date.valueOf(aluno.getDataMatricula()));
            stmt.setString(10, aluno.getStatus().name());
            stmt.setBoolean(11, aluno.isAtivo());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public void atualizar(Aluno aluno) throws SQLException {
        String sql = "UPDATE alunos SET nome_completo = ?, cpf = ?, data_nascimento = ?, telefone = ?, email = ?, endereco = ?, sexo = ?, contato_emergencia = ?, data_matricula = ?, status = ?, ativo = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, aluno.getNomeCompleto());
            stmt.setString(2, aluno.getCpf());
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            stmt.setString(4, aluno.getTelefone());
            stmt.setString(5, aluno.getEmail());
            stmt.setString(6, aluno.getEndereco());
            stmt.setString(7, aluno.getSexo().name());
            stmt.setString(8, aluno.getContatoEmergencia());
            stmt.setDate(9, Date.valueOf(aluno.getDataMatricula()));
            stmt.setString(10, aluno.getStatus().name());
            stmt.setBoolean(11, aluno.isAtivo());
            stmt.setInt(12, aluno.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM alunos WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            resetarAutoIncrement();
        }
    }
    
    public Aluno buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM alunos WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairAluno(rs);
                }
            }
        }
        return null;
    }
    
    public List<Aluno> listarTodos() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos ORDER BY nome_completo";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                alunos.add(extrairAluno(rs));
            }
        }
        return alunos;
    }
    
    public List<Aluno> buscarPorNome(String nome) throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos WHERE nome_completo LIKE ? ORDER BY nome_completo";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    alunos.add(extrairAluno(rs));
                }
            }
        }
        return alunos;
    }
    
    public Aluno buscarPorCPF(String cpf) throws SQLException {
        String sql = "SELECT * FROM alunos WHERE cpf = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairAluno(rs);
                }
            }
        }
        return null;
    }
    
    public List<Aluno> buscarPorStatus(StatusAluno status) throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos WHERE status = ? ORDER BY nome_completo";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    alunos.add(extrairAluno(rs));
                }
            }
        }
        return alunos;
    }
    
    private Aluno extrairAluno(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno();
        aluno.setId(rs.getInt("id"));
        aluno.setNomeCompleto(rs.getString("nome_completo"));
        aluno.setCpf(rs.getString("cpf"));
        aluno.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        aluno.setTelefone(rs.getString("telefone"));
        aluno.setEmail(rs.getString("email"));
        aluno.setEndereco(rs.getString("endereco"));
        
        String sexoStr = rs.getString("sexo");
        if (sexoStr != null) {
            aluno.setSexo(Sexo.valueOf(sexoStr));
        }
        
        aluno.setContatoEmergencia(rs.getString("contato_emergencia"));
        aluno.setDataMatricula(rs.getDate("data_matricula").toLocalDate());
        
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            aluno.setStatus(StatusAluno.valueOf(statusStr));
        }
        
        aluno.setAtivo(rs.getBoolean("ativo"));
        
        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            aluno.setDataCriacao(dataCriacao.toLocalDateTime().toLocalDate());
        }
        
        return aluno;
    }
    
    public void resetarAutoIncrement() throws SQLException {
        String sql = "SELECT COUNT(*) FROM alunos";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                String resetSql = "ALTER TABLE alunos AUTO_INCREMENT = 1";
                try (PreparedStatement resetStmt = conn.prepareStatement(resetSql)) {
                    resetStmt.executeUpdate();
                    System.out.println("Auto increment resetado para 1");
                }
            }
        }
    }
    
    public int buscarAlunosAusentes() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT a.id) FROM alunos a " +
                     "LEFT JOIN pagamentos p ON a.id = p.aluno_id " +
                     "WHERE a.status = 'ATIVO' " +
                     "AND (p.data_pagamento IS NULL OR p.data_pagamento < DATE_SUB(CURDATE(), INTERVAL 30 DAY))";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}