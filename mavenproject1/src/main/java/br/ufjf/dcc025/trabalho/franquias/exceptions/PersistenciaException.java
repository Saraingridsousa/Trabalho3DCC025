/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.exceptions;

public class PersistenciaException extends FranquiaException {
    
    public PersistenciaException(String message) {
        super(message);
    }
    
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}