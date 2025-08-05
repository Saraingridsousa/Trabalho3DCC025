package br.ufjf.dcc025.trabalho.franquias.model.usuarios;

public class Gerente extends Usuario {
    private Long franquiaId;
    
    public Gerente(String nome, String email, String senha) {
        super(nome, email, senha);
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
}