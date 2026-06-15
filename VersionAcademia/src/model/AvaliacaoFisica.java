// model/AvaliacaoFisica.java
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AvaliacaoFisica {
    private int id;
    private Aluno aluno;
    private Double peso;
    private Double altura;
    private Double imc;
    private Double percentualGordura;
    private Double massaMuscular;
    private LocalDate dataAvaliacao;
    private String observacoes;
    private LocalDateTime dataCriacao;
    
    public AvaliacaoFisica() {}
    
    public AvaliacaoFisica(Aluno aluno, Double peso, Double altura, LocalDate dataAvaliacao) {
        this.aluno = aluno;
        this.peso = peso;
        this.altura = altura;
        this.dataAvaliacao = dataAvaliacao;
        calcularIMC();
    }
    
    public void calcularIMC() {
        if (peso != null && altura != null && altura > 0) {
            this.imc = peso / (altura * altura);
        }
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; calcularIMC(); }
    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; calcularIMC(); }
    public Double getImc() { return imc; }
    public void setImc(Double imc) { this.imc = imc; }
    public Double getPercentualGordura() { return percentualGordura; }
    public void setPercentualGordura(Double percentualGordura) { this.percentualGordura = percentualGordura; }
    public Double getMassaMuscular() { return massaMuscular; }
    public void setMassaMuscular(Double massaMuscular) { this.massaMuscular = massaMuscular; }
    public LocalDate getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDate dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}