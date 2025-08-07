/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.exceptions;

public class EntidadeNaoEncontradaException extends FranquiaException {
    public EntidadeNaoEncontradaException(String entidade, Object id) {
        super(entidade + " não encontrada com ID: " + id);
    }

    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }
}