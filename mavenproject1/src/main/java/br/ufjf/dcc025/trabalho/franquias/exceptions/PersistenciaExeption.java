package br.ufjf.dcc025.trabalho.franquias.exceptions;

public class PersistenciaException extends FranquiaException {
    
    public PersistenciaException(String message) {
        super(message);
    }
    
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}