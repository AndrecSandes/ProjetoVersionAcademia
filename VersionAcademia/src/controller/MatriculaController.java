package controller;

import dao.MatriculaDAO;
import model.Matricula;
import model.Aluno;

import java.sql.SQLException;

public class MatriculaController {
    private MatriculaDAO matriculaDAO;
    
    public MatriculaController() {
        this.matriculaDAO = new MatriculaDAO();
    }
    
    public void inserir(Matricula matricula) throws SQLException {
        matriculaDAO.inserir(matricula);
    }
    
    public void atualizar(Matricula matricula) throws SQLException {
        matriculaDAO.atualizar(matricula);
    }
    
    public Matricula buscarPorAluno(Aluno aluno) throws SQLException {
        return matriculaDAO.buscarPorAluno(aluno);
    }
}