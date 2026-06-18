package dao;

import model.Pagamento;
import model.Pagamento.FormaPagamento;
import model.Pagamento.StatusPagamento;
import model.Aluno;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAO {
    
    public void inserir(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO pagamentos (aluno_id, matricula_id, valor, data_pagamento, " +
                     "forma_pagamento, competencia, status, observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, pagamento.getAluno().getId());
            stmt.setInt(2, pagamento.getMatricula().getId());
            stmt.setDouble(3, pagamento.getValor());
            stmt.setDate(4, Date.valueOf(pagamento.getDataPagamento()));
            stmt.setString(5, pagamento.getFormaPagamento().name());
            stmt.setDate(6, Date.valueOf(pagamento.getCompetencia()));
            stmt.setString(7, pagamento.getStatus().name());
            stmt.setString(8, pagamento.getObservacao());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pagamento.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public List<Pagamento> buscarPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Pagamento> pagamentos = new ArrayList<>();
        // ORDER BY id ASC para ordem crescente (1,2,3...)
        String sql = "SELECT * FROM pagamentos WHERE data_pagamento BETWEEN ? AND ? ORDER BY id ASC";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                AlunoDAO alunoDAO = new AlunoDAO();
                
                while (rs.next()) {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setId(rs.getInt("id"));
                    pagamento.setValor(rs.getDouble("valor"));
                    pagamento.setDataPagamento(rs.getDate("data_pagamento").toLocalDate());
                    pagamento.setFormaPagamento(FormaPagamento.valueOf(rs.getString("forma_pagamento")));
                    pagamento.setCompetencia(rs.getDate("competencia").toLocalDate());
                    pagamento.setStatus(StatusPagamento.valueOf(rs.getString("status")));
                    pagamento.setObservacao(rs.getString("observacao"));
                    
                    int alunoId = rs.getInt("aluno_id");
                    Aluno aluno = alunoDAO.buscarPorId(alunoId);
                    pagamento.setAluno(aluno);
                    
                    pagamentos.add(pagamento);
                }
            }
        }
        return pagamentos;
    }
    
    public List<Pagamento> buscarAtrasados() throws SQLException {
        List<Pagamento> pagamentos = new ArrayList<>();
        String sql = "SELECT * FROM pagamentos WHERE status = 'ATRASADO' AND data_pagamento < CURDATE() ORDER BY id ASC";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Pagamento pagamento = new Pagamento();
                pagamento.setId(rs.getInt("id"));
                pagamento.setValor(rs.getDouble("valor"));
                pagamento.setDataPagamento(rs.getDate("data_pagamento").toLocalDate());
                pagamento.setStatus(StatusPagamento.valueOf(rs.getString("status")));
                pagamentos.add(pagamento);
            }
        }
        return pagamentos;
    }
    
    public boolean verificarPagamentoMes(int alunoId, int mes, int ano) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pagamentos WHERE aluno_id = ? AND MONTH(competencia) = ? AND YEAR(competencia) = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, alunoId);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}