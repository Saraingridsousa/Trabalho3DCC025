/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.usuarios;

public class Vendedor extends Usuario {
    private Long franquiaId;
    private double totalVendas;
    private int quantidadeVendas;
    
    public Vendedor(String nome, String cpf, String email, String senha, Long franquiaId) {
        super(nome, cpf, email, senha);
        this.franquiaId = franquiaId;
        this.totalVendas = 0.0;
        this.quantidadeVendas = 0;
    }
    
    @Override
    public String getTipoUsuario() {
        return "VENDEDOR";
    }
    
    public Long getFranquiaId() {
        return franquiaId;
    }
    
    public double getTotalVendas() {
        return totalVendas;
    }
    
    public int getQuantidadeVendas() {
        return quantidadeVendas;
    }
    
    public void setFranquiaId(Long franquiaId) {
        this.franquiaId = franquiaId;
    }
    
    public void setTotalVendas(double totalVendas) {
        this.totalVendas = totalVendas;
    }
    
    public void setQuantidadeVendas(int quantidadeVendas) {
        this.quantidadeVendas = quantidadeVendas;
    }
    
    public void adicionarVenda(double valorVenda) {
        this.totalVendas += valorVenda;
        this.quantidadeVendas++;
    }
    
    public double calcularTicketMedio() {
        if (quantidadeVendas == 0) {
            return 0.0;
        }
        return totalVendas / quantidadeVendas;
    }
    
    public boolean podeCadastrarPedidos() {
        return true;
    }
}