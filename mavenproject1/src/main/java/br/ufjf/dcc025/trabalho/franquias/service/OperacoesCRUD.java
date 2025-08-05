package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import java.util.List;

public interface OperacoesCRUD<T, ID> {
    
    T cadastrar(T entidade) throws ValidacaoException;
    
    T buscarPorId(ID id);
    
    List<T> listar();
    
    T atualizar(ID id, T entidade) throws ValidacaoException, EntidadeNaoEncontradaException;
    
    boolean remover(ID id) throws ValidacaoException, EntidadeNaoEncontradaException;
}
