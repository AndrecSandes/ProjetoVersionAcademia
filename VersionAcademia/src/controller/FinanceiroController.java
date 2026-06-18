package controller;

import dao.ConexaoBD;
import dao.PagamentoDAO;
import model.Pagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FinanceiroController {
    private PagamentoDAO pagamentoDAO;
    
    public FinanceiroController() {
        this.pagamentoDAO = new PagamentoDAO();
    }
    
    public void registrarPagamento(Pagamento pagamento) throws SQLException {
        pagamentoDAO.inserir(pagamento);
    }
    
    public List<Pagamento> getPagamentosPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        return pagamentoDAO.buscarPorPeriodo(inicio, fim);
    }
    
    public double getTotalArrecadadoPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorPeriodo(inicio, fim);
        return pagamentos.stream().mapToDouble(Pagamento::getValor).sum();
    }
    
    public double getFaturamentoTotalMes(LocalDate data) throws SQLException {
        LocalDate inicioMes = data.withDayOfMonth(1);
        LocalDate fimMes = data.withDayOfMonth(data.lengthOfMonth());
        return getTotalArrecadadoPorPeriodo(inicioMes, fimMes);
    }
    
    public double getTicketMedio() throws SQLException {
        AlunoController alunoController = new AlunoController();
        int totalAlunosAtivos = alunoController.getTotalAlunosAtivos();
        double faturamentoMes = getFaturamentoTotalMes(LocalDate.now());
        
        if (totalAlunosAtivos == 0) return 0;
        return faturamentoMes / totalAlunosAtivos;
    }
    
    public double getPercentualInadimplencia() throws SQLException {
        String sql = "SELECT " +
                     "COUNT(CASE WHEN status = 'ATRASADO' THEN 1 END) * 100.0 / NULLIF(COUNT(*), 0) as inadimplencia " +
                     "FROM pagamentos WHERE competencia >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("inadimplencia");
            }
        }
        return 0;
    }
    
    public double getMargemLucro() throws SQLException {
        double faturamento = getFaturamentoTotalMes(LocalDate.now());
        return faturamento * 0.35;
    }
    
    // NOVO MÉTODO: Verifica se já existe pagamento para o aluno no mês/ano
    public boolean verificarPagamentoMes(int alunoId, int mes, int ano) throws SQLException {
        return pagamentoDAO.verificarPagamentoMes(alunoId, mes, ano);
    }
}