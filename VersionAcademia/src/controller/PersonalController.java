package controller;

import dao.PersonalDAO;
import model.Personal;

import java.sql.SQLException;
import java.util.List;

public class PersonalController {
    private PersonalDAO personalDAO;
    
    public PersonalController() {
        this.personalDAO = new PersonalDAO();
    }
    
    public void salvar(Personal personal) throws SQLException {
        validarPersonal(personal);
        personalDAO.inserir(personal);
    }
    
    public void atualizar(Personal personal) throws SQLException {
        validarPersonal(personal);
        personalDAO.atualizar(personal);
    }
    
    public void excluir(int id) throws SQLException {
        personalDAO.excluir(id);
    }
    
    public Personal buscarPorId(int id) throws SQLException {
        return personalDAO.buscarPorId(id);
    }
    
    public List<Personal> listarTodos() throws SQLException {
        return personalDAO.listarTodos();
    }
    
    public List<Personal> buscarPorNome(String nome) throws SQLException {
        return personalDAO.buscarPorNome(nome);
    }
    
    private void validarPersonal(Personal personal) {
        if (personal.getNome() == null || personal.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do personal é obrigatório");
        }
        
        if (personal.getCpf() == null || personal.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do personal é obrigatório");
        }
        
        if (personal.getDataContratacao() == null) {
            throw new IllegalArgumentException("Data de contratação é obrigatória");
        }
    }
}