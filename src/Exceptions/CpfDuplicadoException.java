package Exceptions;

public class CpfDuplicadoException extends ContaException {
    public CpfDuplicadoException() {
        super("CPF já cadastrado!");
    }
}
