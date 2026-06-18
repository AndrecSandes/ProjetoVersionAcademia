package controller;

import dao.ConexaoBD;
import model.Aluno;
import model.Plano;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioController {
    
    // ==================== RELATÓRIOS DE ALUNOS ====================
    
    public List<Aluno> getAlunosPorStatus(String status) throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos WHERE status = ? ORDER BY id ASC";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Aluno a = new Aluno();
                    a.setId(rs.getInt("id"));
                    a.setNomeCompleto(rs.getString("nome_completo"));
                    a.setCpf(rs.getString("cpf"));
                    a.setTelefone(rs.getString("telefone"));
                    a.setEmail(rs.getString("email"));
                    a.setDataMatricula(rs.getDate("data_matricula").toLocalDate());
                    a.setStatus(Aluno.StatusAluno.valueOf(rs.getString("status")));
                    alunos.add(a);
                }
            }
        }
        return alunos;
    }
    
    public List<Map<String, Object>> getMatriculasVencidas() throws SQLException {
        List<Map<String, Object>> matriculas = new ArrayList<>();
        String sql = "SELECT m.id, m.numero_matricula, a.nome_completo, a.cpf, a.telefone, " +
                     "p.nome as plano_nome, m.proximo_vencimento " +
                     "FROM matriculas m " +
                     "JOIN alunos a ON m.aluno_id = a.id " +
                     "JOIN planos p ON m.plano_id = p.id " +
                     "WHERE m.situacao = 'VENCIDA' " +
                     "ORDER BY m.id ASC";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("numero_matricula", rs.getString("numero_matricula"));
                row.put("aluno_nome", rs.getString("nome_completo"));
                row.put("aluno_cpf", rs.getString("cpf"));
                row.put("aluno_telefone", rs.getString("telefone"));
                row.put("plano_nome", rs.getString("plano_nome"));
                row.put("proximo_vencimento", rs.getDate("proximo_vencimento").toLocalDate());
                matriculas.add(row);
            }
        }
        return matriculas;
    }
    
    // ==================== RELATÓRIOS FINANCEIROS ====================
    
    public List<Map<String, Object>> getReceitaMensal(int ano) throws SQLException {
        List<Map<String, Object>> receitas = new ArrayList<>();
        String sql = "SELECT MONTH(data_pagamento) as mes, SUM(valor) as total, COUNT(*) as quantidade " +
                     "FROM pagamentos WHERE YEAR(data_pagamento) = ? AND status = 'PAGO' " +
                     "GROUP BY MONTH(data_pagamento) ORDER BY mes ASC";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ano);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("mes", rs.getInt("mes"));
                    row.put("total", rs.getDouble("total"));
                    row.put("quantidade", rs.getInt("quantidade"));
                    receitas.add(row);
                }
            }
        }
        return receitas;
    }
    
    public List<Map<String, Object>> getReceitaAnual() throws SQLException {
        List<Map<String, Object>> receitas = new ArrayList<>();
        String sql = "SELECT YEAR(data_pagamento) as ano, SUM(valor) as total, COUNT(*) as quantidade " +
                     "FROM pagamentos WHERE status = 'PAGO' " +
                     "GROUP BY YEAR(data_pagamento) ORDER BY ano DESC";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ano", rs.getInt("ano"));
                row.put("total", rs.getDouble("total"));
                row.put("quantidade", rs.getInt("quantidade"));
                receitas.add(row);
            }
        }
        return receitas;
    }
    
    public List<Map<String, Object>> getPlanosMaisVendidos() throws SQLException {
        List<Map<String, Object>> planos = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, p.valor, COUNT(m.id) as total_alunos " +
                     "FROM planos p " +
                     "LEFT JOIN matriculas m ON p.id = m.plano_id AND m.situacao = 'ATIVA' " +
                     "GROUP BY p.id, p.nome, p.valor " +
                     "ORDER BY total_alunos DESC, p.id ASC";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("nome", rs.getString("nome"));
                row.put("valor", rs.getDouble("valor"));
                row.put("total_alunos", rs.getInt("total_alunos"));
                planos.add(row);
            }
        }
        return planos;
    }
    
    // ==================== RELATÓRIOS DE FREQUÊNCIA ====================
    
    public List<Map<String, Object>> getFrequenciaAlunos(int mes, int ano) throws SQLException {
        List<Map<String, Object>> frequencia = new ArrayList<>();
        
        String sql = "SELECT a.id, a.nome_completo, a.cpf, COUNT(p.id) as quantidade_pagamentos " +
                     "FROM alunos a " +
                     "LEFT JOIN pagamentos p ON a.id = p.aluno_id " +
                     "AND MONTH(p.data_pagamento) = ? AND YEAR(p.data_pagamento) = ? " +
                     "AND p.status = 'PAGO' " +
                     "WHERE a.status = 'ATIVO' " +
                     "GROUP BY a.id, a.nome_completo, a.cpf " +
                     "ORDER BY a.id ASC";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mes);
            stmt.setInt(2, ano);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("nome", rs.getString("nome_completo"));
                    row.put("cpf", rs.getString("cpf"));
                    row.put("frequencia", rs.getInt("quantidade_pagamentos"));
                    
                    int freq = rs.getInt("quantidade_pagamentos");
                    if (freq >= 4) {
                        row.put("classificacao", "Excelente");
                    } else if (freq >= 2) {
                        row.put("classificacao", "Regular");
                    } else if (freq == 1) {
                        row.put("classificacao", "Baixa");
                    } else {
                        row.put("classificacao", "Sem presenca");
                    }
                    frequencia.add(row);
                }
            }
        }
        return frequencia;
    }
    
    public double getTotalReceitaPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        String sql = "SELECT COALESCE(SUM(valor), 0) as total FROM pagamentos " +
                     "WHERE data_pagamento BETWEEN ? AND ? AND status = 'PAGO'";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0;
    }
}