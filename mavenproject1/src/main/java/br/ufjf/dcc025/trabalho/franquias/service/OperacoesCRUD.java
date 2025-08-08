/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import java.util.List;

public interface OperacoesCRUD<T, ID> {
    
    T cadastrar(T entidade) throws ValidacaoException;
    
    T buscarPorId(ID id) throws EntidadeNaoEncontradaException;
    
    List<T> listarTodos();
    
    T atualizar(T entidade) throws ValidacaoException, EntidadeNaoEncontradaException;
    
    boolean remover(ID id) throws EntidadeNaoEncontradaException;
    
    boolean validar(T entidade) throws ValidacaoException;
}
