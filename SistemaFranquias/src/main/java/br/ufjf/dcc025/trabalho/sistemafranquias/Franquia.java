package br.ufjf.dcc025.trabalho.sistemafranquias;

import java.util.*;

public class Franquia {
    private String nomeRede;
    private List<Unidade> unidades = new ArrayList<>();

    public void adicionarUnidade(Unidade unidade) {
        unidades.add(unidade);
    }
    
    public Unidade buscarUnidadePorNome(String nome) {
        for (Unidade u : unidades) {
            if (u.getNome().equalsIgnoreCase(nome)) return u;
        }
        return null;
    }
    
    public List<Unidade> getUnidades() {
        return unidades;
    }

    public String getNomeRede() {
        return nomeRede;
    }

    public void setNomeRede(String nomeRede) {
        this.nomeRede = nomeRede;
    }
}

