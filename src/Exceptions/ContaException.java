package Exceptions;

public class ContaException extends RuntimeException {

    public ContaException(String message) {
        super("Exceção encontrada: " + message);
    }

}
