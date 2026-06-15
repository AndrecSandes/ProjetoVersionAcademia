package controller;

import dao.PlanoDAO;
import model.Plano;

import java.sql.SQLException;
import java.util.List;

public class PlanoController {
    private PlanoDAO planodAO;
    
    public PlanoController() {
        this.planodAO = new PlanoDAO();
    }
    
    public void salvar(Plano plano) throws SQLException {
        validarPlano(plano);
        planodAO.inserir(plano);
    }
    
    public void atualizar(Plano plano) throws SQLException {
        validarPlano(plano);
        planodAO.atualizar(plano);
    }
    
    public void excluir(int id) throws SQLException {
        planodAO.excluir(id);
    }
    
    public Plano buscarPorId(int id) throws SQLException {
        return planodAO.buscarPorId(id);
    }
    
    public List<Plano> listarTodos() throws SQLException {
        return planodAO.listarTodos();
    }
    
    private void validarPlano(Plano plano) {
        if (plano.getNome() == null || plano.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano é obrigatório");
        }
        
        if (plano.getValor() <= 0) {
            throw new IllegalArgumentException("Valor do plano deve ser maior que zero");
        }
        
        if (plano.getDuracao() == null) {
            throw new IllegalArgumentException("Duração do plano é obrigatória");
        }
    }
}