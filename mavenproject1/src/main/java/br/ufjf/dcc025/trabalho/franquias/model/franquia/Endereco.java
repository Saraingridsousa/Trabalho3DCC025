/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.model.franquia;

import java.io.Serializable;

public class Endereco implements Serializable {
    private String logradouro;
    private String cidade;
    private String estado;
    private String cep;
    
    public Endereco(String logradouro, String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }
    
    public String getEnderecoCompleto() {
        return logradouro + ", " + cidade + " - " + estado + ", " + cep;
    }
    
    public String getLogradouro() {
        return logradouro;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public String getCep() {
        return cep;
    }
    
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }
    
    @Override
    public String toString() {
        return getEnderecoCompleto();
    }
}