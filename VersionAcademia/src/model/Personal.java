// model/Personal.java
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Personal {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String especialidade;
    private String horarioTrabalho;
    private boolean ativo;
    private LocalDate dataContratacao;
    private LocalDateTime dataCriacao;
    
    public Personal() {}
    
    public Personal(String nome, String cpf, String especialidade, LocalDate dataContratacao) {
        this.nome = nome;
        this.cpf = cpf;
        this.especialidade = especialidade;
        this.dataContratacao = dataContratacao;
        this.ativo = true;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getHorarioTrabalho() { return horarioTrabalho; }
    public void setHorarioTrabalho(String horarioTrabalho) { this.horarioTrabalho = horarioTrabalho; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}