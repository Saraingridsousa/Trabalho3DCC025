package br.ufjf.dcc025.trabalho.franquias.exceptions;

public class FranquiaException extends Exception {
    
    public FranquiaException(String message) {
        super(message);
    }
    
    public FranquiaException(String message, Throwable cause) {
        super(message, cause);
    }
}