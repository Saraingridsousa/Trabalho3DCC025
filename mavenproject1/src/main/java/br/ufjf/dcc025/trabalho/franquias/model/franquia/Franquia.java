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
    private String cidade;
    private String estado;
    private String cep;
    private Gerente gerente;
    private List<Vendedor> vendedores;
    private List<Produto> produtos;
    private double receitaAcumulada;
    private int totalPedidos;
    
    public Franquia() {
        this.vendedores = new ArrayList<>();
        this.produtos = new ArrayList<>();
        this.receitaAcumulada = 0.0;
        this.totalPedidos = 0;
    }
    
    public Franquia(String nome, Endereco endereco) {
        this();
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
        this.cep = endereco.getCep();
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Gerente getGerente() {
        return gerente;
    }

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }

    public List<Vendedor> getVendedores() {
        return vendedores;
    }

    public void setVendedores(List<Vendedor> vendedores) {
        this.vendedores = vendedores;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
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

    public double calcularTicketMedio() {
        if (totalPedidos == 0) {
            return 0.0;
        }
        return receitaAcumulada / totalPedidos;
    }
    
    public void registrarVenda(double valor) {
        this.receitaAcumulada += valor;
        this.totalPedidos++;
    }
    
    public String getEnderecoCompleto() {
        return endereco.getEnderecoCompleto();
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