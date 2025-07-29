package br.ufjf.dcc025.trabalho.sistemafranquias;

public class Vendedor extends Usuario {
    public Vendedor(String nome, String cpf, String email, String senha) {
        super(nome, cpf, email, senha);
    }
    
    @Override
    public String getTipo() {
        return "Vendedor";
    }
}
