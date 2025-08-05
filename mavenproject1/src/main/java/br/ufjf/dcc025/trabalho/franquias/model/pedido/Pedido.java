package br.ufjf.dcc025.trabalho.franquias.model.pedido;

import br.ufjf.dcc025.trabalho.franquias.model.produto.ItemPedido;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Franquia;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {
    
    // Enum para status do pedido
    public enum StatusPedido {
        EM_ANDAMENTO, FINALIZADO, CANCELADO
    }
    
    private Long id;
    private Cliente cliente;
    private Vendedor vendedor;
    private Franquia franquia;
    private List<ItemPedido> itens; // Coleção
    private LocalDate dataCriacao;
    private LocalDate dataFinalizacao;
    private StatusPedido status; // Uso do enum
    
    public Pedido(Cliente cliente, Vendedor vendedor, Franquia franquia) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.franquia = franquia;
        this.itens = new ArrayList<>();
        this.dataCriacao = LocalDate.now();
        this.status = StatusPedido.EM_ANDAMENTO;
    }
    
    // Método para calcular total
    public double getTotal() {
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }
    
    // Método para adicionar item
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }
    
    // Método para remover item
    public void removerItem(ItemPedido item) {
        itens.remove(item);
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Vendedor getVendedor() {
        return vendedor;
    }
    
    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
    
    public Franquia getFranquia() {
        return franquia;
    }
    
    public void setFranquia(Franquia franquia) {
        this.franquia = franquia;
    }
    
    public List<ItemPedido> getItens() {
        return new ArrayList<>(itens); // Retorna cópia para proteger encapsulamento
    }
    
    public LocalDate getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDate getDataFinalizacao() {
        return dataFinalizacao;
    }
    
    public void setDataFinalizacao(LocalDate dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }
    
    public StatusPedido getStatus() {
        return status;
    }
    
    public void setStatus(StatusPedido status) {
        this.status = status;
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