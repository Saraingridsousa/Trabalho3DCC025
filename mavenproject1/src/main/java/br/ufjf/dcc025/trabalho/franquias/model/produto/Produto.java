/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.produto;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nome;
    private String descricao;
    private double preco;
    private int estoque;
    private int estoqueMinimo;
    private String categoria;
    
    public Produto() {
    }
    
    public Produto(String nome, String descricao, double preco, int quantidadeEstoque, int estoqueMinimo, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = quantidadeEstoque;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria;
    }

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public boolean isEstoqueBaixo() {
        return estoque <= estoqueMinimo;
    }
    
    public boolean reduzirEstoque(int quantidade) {
        if (quantidade > 0 && quantidade <= estoque) {
            this.estoque -= quantidade;
            return true;
        }
        return false;
    }
    
    public void adicionarEstoque(int quantidade) {
        if (quantidade > 0) {
            this.estoque += quantidade;
        }
    }
    
    public boolean isDisponivel(int quantidadeDesejada) {
        return estoque >= quantidadeDesejada;
    }
    
    @Override
    public String toString() {
        return id + " - " + nome +
                " = R$" + preco +
                " (" + estoque +
                "  restantes)";
    }
}