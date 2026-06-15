// model/Pagamento.java
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pagamento {
    private int id;
    private Aluno aluno;
    private Matricula matricula;
    private double valor;
    private LocalDate dataPagamento;
    private FormaPagamento formaPagamento;
    private LocalDate competencia;
    private StatusPagamento status;
    private String observacao;
    private LocalDateTime dataCriacao;
    
    public enum FormaPagamento {
        PIX, DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, TRANSFERENCIA
    }
    
    public enum StatusPagamento {
        PAGO, PENDENTE, ATRASADO
    }
    
    public Pagamento() {}
    
    public Pagamento(Aluno aluno, Matricula matricula, double valor, 
                     FormaPagamento formaPagamento, LocalDate competencia) {
        this.aluno = aluno;
        this.matricula = matricula;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.competencia = competencia;
        this.dataPagamento = LocalDate.now();
        this.status = StatusPagamento.PAGO;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Matricula getMatricula() { return matricula; }
    public void setMatricula(Matricula matricula) { this.matricula = matricula; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
    public LocalDate getCompetencia() { return competencia; }
    public void setCompetencia(LocalDate competencia) { this.competencia = competencia; }
    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}