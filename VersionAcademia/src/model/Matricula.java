// model/Matricula.java
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Matricula {
    private int id;
    private String numeroMatricula;
    private Aluno aluno;
    private Plano plano;
    private LocalDate dataMatricula;
    private LocalDate proximoVencimento;
    private SituacaoMatricula situacao;
    private boolean renovacaoAutomatica;
    private LocalDate dataCancelamento;
    private LocalDateTime dataCriacao;
    
    public enum SituacaoMatricula {
        ATIVA, VENCIDA, CANCELADA
    }
    
    public Matricula() {}
    
    public Matricula(Aluno aluno, Plano plano, LocalDate dataMatricula) {
        this.aluno = aluno;
        this.plano = plano;
        this.dataMatricula = dataMatricula;
        this.proximoVencimento = calcularProximoVencimento(dataMatricula, plano);
        this.situacao = SituacaoMatricula.ATIVA;
        this.renovacaoAutomatica = false;
        this.numeroMatricula = gerarNumeroMatricula();
    }
    
    private String gerarNumeroMatricula() {
        return "MAT" + System.currentTimeMillis();
    }
    
    private LocalDate calcularProximoVencimento(LocalDate dataInicio, Plano plano) {
        return dataInicio.plusMonths(plano.getMesesDuracao());
    }
    
    public void renovar() {
        if (renovacaoAutomatica && situacao == SituacaoMatricula.ATIVA) {
            this.proximoVencimento = proximoVencimento.plusMonths(plano.getMesesDuracao());
        }
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumeroMatricula() { return numeroMatricula; }
    public void setNumeroMatricula(String numeroMatricula) { this.numeroMatricula = numeroMatricula; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Plano getPlano() { return plano; }
    public void setPlano(Plano plano) { this.plano = plano; }
    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }
    public LocalDate getProximoVencimento() { return proximoVencimento; }
    public void setProximoVencimento(LocalDate proximoVencimento) { this.proximoVencimento = proximoVencimento; }
    public SituacaoMatricula getSituacao() { return situacao; }
    public void setSituacao(SituacaoMatricula situacao) { this.situacao = situacao; }
    public boolean isRenovacaoAutomatica() { return renovacaoAutomatica; }
    public void setRenovacaoAutomatica(boolean renovacaoAutomatica) { this.renovacaoAutomatica = renovacaoAutomatica; }
    public LocalDate getDataCancelamento() { return dataCancelamento; }
    public void setDataCancelamento(LocalDate dataCancelamento) { this.dataCancelamento = dataCancelamento; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}