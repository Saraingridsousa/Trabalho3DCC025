/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.produto;

import java.io.Serializable;

public class ItemPedido implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;
    private String observacoes;
    
    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();
    }

    public double getSubtotal() {
        return precoUnitario * quantidade;
    }
    
    public boolean isValido() {
        return produto != null && quantidade > 0 && precoUnitario >= 0;
    }
    
    public Long getId() {
        return id;
    }
    
    public Produto getProduto() {
        return produto;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public double getPrecoUnitario() {
        return precoUnitario;
    }
    
    public String getObservacoes() {
        return observacoes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        this.precoUnitario = produto.getPreco();
    }
    
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
        
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    @Override
    public String toString() {
        return "ItemPedido{" +
                "produto=" + produto.getNome() +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}