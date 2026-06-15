// model/Aluno.java
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Aluno {
    private int id;
    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String endereco;
    private Sexo sexo;
    private String contatoEmergencia;
    private LocalDate dataMatricula;
    private StatusAluno status;
    private boolean ativo;
    private LocalDate dataCriacao;
    
    // Relacionamentos
    private Matricula matricula;
    private List<AvaliacaoFisica> avaliacoes = new ArrayList<>();
    private List<Pagamento> pagamentos = new ArrayList<>();
    
    public enum Sexo {
        MASCULINO, FEMININO, OUTRO
    }
    
    public enum StatusAluno {
        ATIVO, INATIVO, PENDENTE
    }
    
    // Construtores
    public Aluno() {}
    
    public Aluno(String nomeCompleto, String cpf, LocalDate dataNascimento, 
                 String telefone, String email, Sexo sexo, LocalDate dataMatricula) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.sexo = sexo;
        this.dataMatricula = dataMatricula;
        this.status = StatusAluno.PENDENTE;
        this.ativo = true;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public String getContatoEmergencia() { return contatoEmergencia; }
    public void setContatoEmergencia(String contatoEmergencia) { this.contatoEmergencia = contatoEmergencia; }
    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }
    public StatusAluno getStatus() { return status; }
    public void setStatus(StatusAluno status) { this.status = status; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
    public Matricula getMatricula() { return matricula; }
    public void setMatricula(Matricula matricula) { this.matricula = matricula; }
    public List<AvaliacaoFisica> getAvaliacoes() { return avaliacoes; }
    public void setAvaliacoes(List<AvaliacaoFisica> avaliacoes) { this.avaliacoes = avaliacoes; }
    public List<Pagamento> getPagamentos() { return pagamentos; }
    public void setPagamentos(List<Pagamento> pagamentos) { this.pagamentos = pagamentos; }
}