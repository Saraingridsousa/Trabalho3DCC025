package br.ufjf.dcc025.trabalho.sistemafranquias;

public abstract class Usuario {
    private String nome;
    private String cpf;
    private String email;
    private String senha;

    public Usuario(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }

    public abstract String getTipo();
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }    
    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setEmail(String email) { this.email = email; }    
    public void setSenha(String senha) { this.senha = senha; }
}
