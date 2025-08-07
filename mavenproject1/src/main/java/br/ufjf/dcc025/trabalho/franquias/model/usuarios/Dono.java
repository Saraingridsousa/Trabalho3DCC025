/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.usuarios;

public class Dono extends Usuario {
        
    public Dono(String nome, String cpf, String email, String senha) {
        super(nome, cpf, email, senha);
    }
    
    @Override
    public String getTipoUsuario() {
        return "DONO";
    }
    
    public boolean podeGerenciarFranquias() {
        return true;
    }
    
    public boolean podeGerenciarGerentes() {
        return true;
    }
    
    public boolean podeAcessarRelatoriosConsolidados() {
        return true;
    }
}
