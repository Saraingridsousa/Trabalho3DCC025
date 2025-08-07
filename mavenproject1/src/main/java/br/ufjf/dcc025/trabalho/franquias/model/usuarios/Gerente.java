/*
 * Autores: Sara Ingrid - 202376049, Angélica Coutinho - 202376046
 */
package br.ufjf.dcc025.trabalho.franquias.model.usuarios;

public class Gerente extends Usuario {
    private Long franquiaId;
        
    public Gerente(String nome, String cpf, String email, String senha, Long franquiaId) {
        super(nome, cpf, email, senha);
        this.franquiaId = franquiaId;
    }
    
    @Override
    public String getTipoUsuario() {
        return "GERENTE";
    }
    
    public Long getFranquiaId() {
        return franquiaId;
    }
    
    public void setFranquiaId(Long franquiaId) {
        this.franquiaId = franquiaId;
    }
    
    public boolean podeGerenciarVendedores() {
        return true;
    }
    
    public boolean podeGerenciarPedidos() {
        return true;
    }
    
    public boolean podeGerenciarEstoque() {
        return true;
    }
    
    public boolean podeAcessarRelatoriosFranquia() {
        return true;
    }
}