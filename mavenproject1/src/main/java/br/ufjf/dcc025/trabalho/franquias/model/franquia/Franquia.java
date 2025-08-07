/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.franquia;

import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Gerente;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Franquia implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nome;
    private Endereco endereco;
    private Gerente gerente;
    private List<Vendedor> vendedores;
    private List<Produto> produtos;
    private double receitaAcumulada;
    private int totalPedidos;
    
    public Franquia(String nome, Endereco endereco, Gerente gerente) {
        this.nome = nome;
        this.endereco = endereco;
        this.gerente = gerente;
        this.vendedores = new ArrayList<>();
        this.produtos = new ArrayList<>();
        this.receitaAcumulada = 0.0;
        this.totalPedidos = 0;
    }
    
    public double getTicketMedio() {
        return totalPedidos == 0 ? 0.0 : receitaAcumulada / totalPedidos;
    }
    
    public void adicionarVenda(double valor) {
        this.receitaAcumulada += valor;
        this.totalPedidos++;
    }
    
    public void adicionarVendedor(Vendedor vendedor) {
        if (vendedor != null) {
            vendedor.setFranquiaId(this.id);
            this.vendedores.add(vendedor);
        }
    }
    
    public boolean removerVendedor(Vendedor vendedor) {
        return this.vendedores.remove(vendedor);
    }
    
    public void adicionarProduto(Produto produto) {
        if (produto != null) {
            this.produtos.add(produto);
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Gerente getGerente() {
        return gerente;
    }

    public List<Vendedor> getVendedores() {
        return vendedores;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public double getReceitaAcumulada() {
        return receitaAcumulada;
    }

    public int getTotalPedidos() {
        return totalPedidos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }

    public void setVendedores(List<Vendedor> vendedores) {
        this.vendedores = vendedores;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public void setReceitaAcumulada(double receitaAcumulada) {
        this.receitaAcumulada = receitaAcumulada;
    }

    public void setTotalPedidos(int totalPedidos) {
        this.totalPedidos = totalPedidos;
    }
    
    @Override
    public String toString() {
        return "Franquia{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco=" + endereco.toString() + '\'' +
                ", gerente=" + (gerente != null ? gerente.getNome() : "Não atribuído") +
                ", vendedores=" + vendedores.size() +
                ", receita=" + receitaAcumulada +
                '}';
    }
}