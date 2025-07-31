package br.ufjf.dcc025.trabalho.sistemafranquias;

public class Gerente extends Usuario {
    public Gerente(String nome, String cpf, String email, String senha) {
        super(nome, cpf, email, senha);
    }

    @Override
    public String getTipo() {
        return "Gerente";
    }
}
