// controller/AlunoController.java
package controller;

import dao.AlunoDAO;
import model.Aluno;
import model.Aluno.StatusAluno;

import java.sql.SQLException;
import java.util.List;

public class AlunoController {
    private AlunoDAO alunoDAO;
    
    public AlunoController() {
        this.alunoDAO = new AlunoDAO();
    }
    
    public void salvar(Aluno aluno) throws SQLException {
        validarAluno(aluno);
        alunoDAO.inserir(aluno);
    }
    
    public void atualizar(Aluno aluno) throws SQLException {
        validarAluno(aluno);
        alunoDAO.atualizar(aluno);
    }
    
    public void excluir(int id) throws SQLException {
        alunoDAO.excluir(id);
    }
    
    public Aluno buscarPorId(int id) throws SQLException {
        return alunoDAO.buscarPorId(id);
    }
    
    public List<Aluno> listarTodos() throws SQLException {
        return alunoDAO.listarTodos();
    }
    
    public List<Aluno> buscarPorNome(String nome) throws SQLException {
        return alunoDAO.buscarPorNome(nome);
    }
    
    public Aluno buscarPorCPF(String cpf) throws SQLException {
        return alunoDAO.buscarPorCPF(cpf);
    }
    
    public List<Aluno> buscarPorStatus(StatusAluno status) throws SQLException {
        return alunoDAO.buscarPorStatus(status);
    }
    
    private void validarAluno(Aluno aluno) {
        if (aluno.getNomeCompleto() == null || aluno.getNomeCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do aluno é obrigatório");
        }
        
        if (aluno.getCpf() == null || aluno.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do aluno é obrigatório");
        }
        
        if (aluno.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }
    }

    public int getTotalAlunosAtivos() throws SQLException {
        return alunoDAO.buscarPorStatus(StatusAluno.ATIVO).size();
    }

    public int getTotalAlunosInativos() throws SQLException {
        return alunoDAO.buscarPorStatus(StatusAluno.INATIVO).size();
    }

    public int getTotalAlunosPendentes() throws SQLException {
        return alunoDAO.buscarPorStatus(StatusAluno.PENDENTE).size();
    }

    public int getAlunosAusentes() throws SQLException {
        // Alunos que não têm pagamento nos últimos 30 dias
        return alunoDAO.buscarAlunosAusentes();
    }

    public void atualizarStatus(int id, String novoStatus) throws SQLException {
        Aluno aluno = alunoDAO.buscarPorId(id);
        if (aluno != null) {
            aluno.setStatus(StatusAluno.valueOf(novoStatus));
            alunoDAO.atualizar(aluno);
        }
    }
}