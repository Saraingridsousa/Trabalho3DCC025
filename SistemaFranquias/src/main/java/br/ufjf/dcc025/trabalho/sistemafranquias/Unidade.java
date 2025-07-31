package br.ufjf.dcc025.trabalho.sistemafranquias;

import java.util.List;

public class Unidade {
    private String nome;
    private String endereco;
    private List<Vendedor> vendedores;
    private List<Pedido> pedidos;
    private List<Produto> produtos;

    public Unidade(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public List<Vendedor> getVendedores() { return vendedores; }
    public List<Pedido> getPedidos() { return pedidos; }
    public List<Produto> getProdutos() { return produtos; }

    public void setNome(String nome) { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setVendedores(List<Vendedor> vendedores) { this.vendedores = vendedores; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }

}
