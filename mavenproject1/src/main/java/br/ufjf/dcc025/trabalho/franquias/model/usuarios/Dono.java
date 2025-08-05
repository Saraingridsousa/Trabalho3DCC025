package br.ufjf.dcc025.trabalho.franquias.model.usuarios;

public class Dono extends Usuario {
    
    public Dono(String nome, String email, String senha) {
        super(nome, email, senha);
    }
    
    @Override
    public String getTipoUsuario() {
        return "DONO";
    }
}