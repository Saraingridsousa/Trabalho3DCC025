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
    
    public Produto(String nome, String descricao, double preco, int estoque, int estoqueMinimo, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria;
    }

    public boolean isDisponivel(int quantidadeDesejada) {
        return estoque >= quantidadeDesejada;
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

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", estoque=" + estoque +
                '}';
    }
}