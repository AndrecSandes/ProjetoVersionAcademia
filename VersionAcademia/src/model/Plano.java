// model/Plano.java
package model;

import java.time.LocalDateTime;

public class Plano {
    private int id;
    private String nome;
    private double valor;
    private Duracao duracao;
    private String beneficios;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    
    public enum Duracao {
        MENSAL, TRIMESTRAL, SEMESTRAL, ANUAL
    }
    
    public Plano() {}
    
    public Plano(String nome, double valor, Duracao duracao, String beneficios) {
        this.nome = nome;
        this.valor = valor;
        this.duracao = duracao;
        this.beneficios = beneficios;
        this.ativo = true;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public Duracao getDuracao() { return duracao; }
    public void setDuracao(Duracao duracao) { this.duracao = duracao; }
    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public int getMesesDuracao() {
        switch (duracao) {
            case MENSAL: return 1;
            case TRIMESTRAL: return 3;
            case SEMESTRAL: return 6;
            case ANUAL: return 12;
            default: return 1;
        }
    }
}