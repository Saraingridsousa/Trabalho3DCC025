/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.pedido;

import br.ufjf.dcc025.trabalho.franquias.model.produto.ItemPedido;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Franquia;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {
    
    public enum StatusPedido {
        EM_ANDAMENTO, ENTREGUE, CANCELADO, PENDENTE, APROVADO
    }
    
    private Long id;
    private Cliente cliente;
    private Vendedor vendedor;
    private Franquia franquia;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataFinalizacao;
    private List<ItemPedido> itens;
    private String formaPagamento;
    private String modalidadeEntrega;
    private double valorTotal;
    private double taxaEntrega;
    private double taxaServico;
    private StatusPedido status;
    
    public Pedido(Cliente cliente, Vendedor vendedor, Franquia franquia) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.franquia = franquia;
        this.itens = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.taxaEntrega = 0.0;
        this.taxaServico = 0.0;
    }
    
    public double getTotal() {
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }
    
    public void adicionarItem(ItemPedido item) {
        if (item != null) {
            this.itens.add(item);
            calcularValorTotal();
        }
    }
    
    public boolean removerItem(ItemPedido item) {
        boolean removido = this.itens.remove(item);
        if (removido) {
            calcularValorTotal();
        }
        return removido;
    }
    
    public void calcularValorTotal() {
        double subtotal = itens.stream()
                .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
                .sum();
        this.valorTotal = subtotal + taxaEntrega + taxaServico;
    }
    
    public double getSubtotal() {
        return itens.stream()
                .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
                .sum();
    }
    
    public boolean isValido() {
        return cliente.getNome() != null && !cliente.getNome().trim().isEmpty() &&
               !itens.isEmpty() && 
               formaPagamento != null && !formaPagamento.trim().isEmpty() &&
               modalidadeEntrega != null && !modalidadeEntrega.trim().isEmpty() &&
               vendedor != null;
    }
    
    public Long getId() {
        return id;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public String getNomeCliente() {
        return cliente.getNome();
    }
    
    public Vendedor getVendedor() {
        return vendedor;
    }
    
    public Franquia getFranquia() {
        return franquia;
    }
    
    public List<ItemPedido> getItens() {
        return new ArrayList<>(itens); // Retorna cópia para proteger encapsulamento
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public LocalDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }
    
    public StatusPedido getStatus() {
        return status;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public String getModalidadeEntrega() {
        return modalidadeEntrega;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }

    public double getTaxaServico() {
        return taxaServico;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
    
    public void setFranquia(Franquia franquia) {
        this.franquia = franquia;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }
    
    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
           
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setModalidadeEntrega(String modalidadeEntrega) {
        this.modalidadeEntrega = modalidadeEntrega;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setTaxaEntrega(double taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public void setTaxaServico(double taxaServico) {
        this.taxaServico = taxaServico;
    }
    
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + cliente.getNome() +
                ", vendedor=" + vendedor.getNome() +
                ", total=" + getTotal() +
                ", status=" + status +
                ", itens=" + itens.size() +
                '}';
    }
}