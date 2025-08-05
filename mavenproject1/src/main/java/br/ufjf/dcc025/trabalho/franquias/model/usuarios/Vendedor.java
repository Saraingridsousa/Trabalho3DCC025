package br.ufjf.dcc025.trabalho.franquias.model.usuarios;

public class Vendedor extends Usuario {
    private Long franquiaId;
    
    public Vendedor(String nome, String email, String senha) {
        super(nome, email, senha);
    }
    
    @Override
    public String getTipoUsuario() {
        return "VENDEDOR";
    }
    
    public Long getFranquiaId() {
        return franquiaId;
    }
    
    public void setFranquiaId(Long franquiaId) {
        this.franquiaId = franquiaId;
    }
}