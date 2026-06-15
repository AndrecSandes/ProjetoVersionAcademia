package dao;

import model.Matricula;
import model.Matricula.SituacaoMatricula;
import model.Aluno;
import model.Plano;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO {
    private AlunoDAO alunoDAO = new AlunoDAO();
    
    public void inserir(Matricula matricula) throws SQLException {
        String sql = "INSERT INTO matriculas (numero_matricula, aluno_id, plano_id, data_matricula, " +
                     "proximo_vencimento, situacao, renovacao_automatica) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, matricula.getNumeroMatricula());
            stmt.setInt(2, matricula.getAluno().getId());
            stmt.setInt(3, matricula.getPlano().getId());
            stmt.setDate(4, Date.valueOf(matricula.getDataMatricula()));
            stmt.setDate(5, Date.valueOf(matricula.getProximoVencimento()));
            stmt.setString(6, matricula.getSituacao().name());
            stmt.setBoolean(7, matricula.isRenovacaoAutomatica());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    matricula.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public Matricula buscarPorAluno(Aluno aluno) throws SQLException {
        String sql = "SELECT * FROM matriculas WHERE aluno_id = ? AND situacao = 'ATIVA' ORDER BY data_matricula DESC LIMIT 1";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aluno.getId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairMatricula(rs);
                }
            }
        }
        return null;
    }
    
    private Matricula extrairMatricula(ResultSet rs) throws SQLException {
        Matricula matricula = new Matricula();
        matricula.setId(rs.getInt("id"));
        matricula.setNumeroMatricula(rs.getString("numero_matricula"));
        
        Aluno aluno = alunoDAO.buscarPorId(rs.getInt("aluno_id"));
        matricula.setAluno(aluno);
        
        PlanoDAO planoDAO = new PlanoDAO();
        Plano plano = planoDAO.buscarPorId(rs.getInt("plano_id"));
        matricula.setPlano(plano);
        
        matricula.setDataMatricula(rs.getDate("data_matricula").toLocalDate());
        matricula.setProximoVencimento(rs.getDate("proximo_vencimento").toLocalDate());
        matricula.setSituacao(SituacaoMatricula.valueOf(rs.getString("situacao")));
        matricula.setRenovacaoAutomatica(rs.getBoolean("renovacao_automatica"));
        
        return matricula;
    }
    
    public void atualizar(Matricula matricula) throws SQLException {
        String sql = "UPDATE matriculas SET plano_id = ?, situacao = ?, renovacao_automatica = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, matricula.getPlano().getId());
            stmt.setString(2, matricula.getSituacao().name());
            stmt.setBoolean(3, matricula.isRenovacaoAutomatica());
            stmt.setInt(4, matricula.getId());
            
            stmt.executeUpdate();
        }
    }
}