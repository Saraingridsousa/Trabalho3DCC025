package br.ufjf.dcc025.trabalho.sistemafranquias;

public class Dono extends Usuario {
    public Dono(String nome, String cpf, String email, String senha) {
        super(nome, cpf, email, senha);
    }

    @Override
    public String getTipo() {
        return "Dono";
    }
}
