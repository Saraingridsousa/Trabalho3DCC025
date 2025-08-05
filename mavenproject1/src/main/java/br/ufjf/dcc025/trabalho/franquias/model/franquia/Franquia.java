package br.ufjf.dcc025.trabalho.franquias.model.franquia;

import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Gerente;
import java.io.Serializable;

public class Franquia implements Serializable {
    private Long id;
    private String nome;
    private Endereco endereco; // Composição
    private Gerente gerente;
    private double receitaAcumulada;
    private int totalPedidos;
    
    public Franquia(String nome, Endereco endereco, Gerente gerente) {
        this.nome = nome;
        this.endereco = endereco;
        this.gerente = gerente;
        this.receitaAcumulada = 0.0;
        this.totalPedidos = 0;
    }
    
    // Método para calcular ticket médio
    public double getTicketMedio() {
        return totalPedidos > 0 ? receitaAcumulada / totalPedidos : 0.0;
    }
    
    // Método para adicionar receita
    public void adicionarReceita(double valor) {
        this.receitaAcumulada += valor;
        this.totalPedidos++;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Endereco getEndereco() {
        return endereco;
    }
    
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    
    public Gerente getGerente() {
        return gerente;
    }
    
    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }
    
    public double getReceitaAcumulada() {
        return receitaAcumulada;
    }
    
    public void setReceitaAcumulada(double receitaAcumulada) {
        this.receitaAcumulada = receitaAcumulada;
    }
    
    public int getTotalPedidos() {
        return totalPedidos;
    }
    
    public void setTotalPedidos(int totalPedidos) {
        this.totalPedidos = totalPedidos;
    }
    
    @Override
    public String toString() {
        return "Franquia{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco=" + endereco +
                ", gerente=" + (gerente != null ? gerente.getNome() : "Não atribuído") +
                ", receita=" + receitaAcumulada +
                '}';
    }
}