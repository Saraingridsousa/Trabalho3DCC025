/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.pedido;

import br.ufjf.dcc025.trabalho.franquias.model.produto.ItemPedido;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nomeCliente;
    private LocalDateTime dataHora;
    private List<ItemPedido> itens;
    private String formaPagamento;
    private String modalidadeEntrega;
    private double valorTotal;
    private double taxaEntrega;
    private double taxaServico;
    private Vendedor vendedor;
    private Long franquiaId;
    private StatusPedido status;
    
    public enum StatusPedido {
        PENDENTE, APROVADO, REJEITADO, ENTREGUE, CANCELADO
    }
    
    public Pedido() {
        this.itens = new ArrayList<>();
        this.dataHora = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.taxaEntrega = 0.0;
        this.taxaServico = 0.0;
    }
    
    public Pedido(String nomeCliente, Vendedor vendedor, Long franquiaId) {
        this();
        this.nomeCliente = nomeCliente;
        this.vendedor = vendedor;
        this.franquiaId = franquiaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getModalidadeEntrega() {
        return modalidadeEntrega;
    }

    public void setModalidadeEntrega(String modalidadeEntrega) {
        this.modalidadeEntrega = modalidadeEntrega;
    }

    public double getTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(double taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public double getTaxaServico() {
        return taxaServico;
    }

    public void setTaxaServico(double taxaServico) {
        this.taxaServico = taxaServico;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Long getFranquiaId() {
        return franquiaId;
    }

    public void setFranquiaId(Long franquiaId) {
        this.franquiaId = franquiaId;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
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
        return nomeCliente != null && !nomeCliente.trim().isEmpty() &&
               !itens.isEmpty() && 
               formaPagamento != null && !formaPagamento.trim().isEmpty() &&
               modalidadeEntrega != null && !modalidadeEntrega.trim().isEmpty() &&
               vendedor != null;
    }
    
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + nomeCliente +
                ", vendedor=" + vendedor.getNome() +
                ", total=" + getTotal() +
                ", status=" + status +
                ", itens=" + itens.size() +
                '}';
    }
}