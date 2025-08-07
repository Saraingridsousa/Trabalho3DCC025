/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.usuarios;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String nome;
    protected String cpf;
    protected String email;
    protected String senha;
    protected Long id;
    
    public Usuario(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Long getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public abstract String getTipoUsuario();
    
    public boolean validarCredenciais(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", tipo='" + getTipoUsuario() + '\'' +
                '}';
    }
}