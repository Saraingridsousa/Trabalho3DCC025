/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.pedido;

import br.ufjf.dcc025.trabalho.franquias.model.franquia.Endereco;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private Endereco endereco;
    private int quantidadeCompras;
    private double valorTotalCompras;
    private LocalDateTime ultimaCompra;
        
    public Cliente(String nome, String email, String telefone, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.quantidadeCompras = 0;
        this.valorTotalCompras = 0.0;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public int getQuantidadeCompras() {
        return quantidadeCompras;
    }

    public double getValorTotalCompras() {
        return valorTotalCompras;
    }

    public LocalDateTime getUltimaCompra() {
        return ultimaCompra;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setQuantidadeCompras(int quantidadeCompras) {
        this.quantidadeCompras = quantidadeCompras;
    }

    public void setValorTotalCompras(double valorTotalCompras) {
        this.valorTotalCompras = valorTotalCompras;
    }

    public void setUltimaCompra(LocalDateTime ultimaCompra) {
        this.ultimaCompra = ultimaCompra;
    }
    
    public void registrarCompra(double valor) {
        this.quantidadeCompras++;
        this.valorTotalCompras += valor;
        this.ultimaCompra = LocalDateTime.now();
    }
    
    public double calcularTicketMedio() {
        if (quantidadeCompras == 0) {
            return 0.0;
        }
        return valorTotalCompras / quantidadeCompras;
    }
    
    public boolean isClienteRecorrente() {
        return quantidadeCompras > 3;
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}