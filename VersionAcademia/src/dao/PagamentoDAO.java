package dao;

import model.Pagamento;
import model.Pagamento.FormaPagamento;
import model.Pagamento.StatusPagamento;

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
        String sql = "SELECT * FROM pagamentos WHERE data_pagamento BETWEEN ? AND ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setId(rs.getInt("id"));
                    pagamento.setValor(rs.getDouble("valor"));
                    pagamento.setDataPagamento(rs.getDate("data_pagamento").toLocalDate());
                    pagamento.setFormaPagamento(FormaPagamento.valueOf(rs.getString("forma_pagamento")));
                    pagamento.setCompetencia(rs.getDate("competencia").toLocalDate());
                    pagamento.setStatus(StatusPagamento.valueOf(rs.getString("status")));
                    pagamento.setObservacao(rs.getString("observacao"));
                    pagamentos.add(pagamento);
                }
            }
        }
        return pagamentos;
    }
    
    public List<Pagamento> buscarAtrasados() throws SQLException {
        List<Pagamento> pagamentos = new ArrayList<>();
        String sql = "SELECT * FROM pagamentos WHERE status = 'ATRASADO' AND data_pagamento < CURDATE()";
        
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
}