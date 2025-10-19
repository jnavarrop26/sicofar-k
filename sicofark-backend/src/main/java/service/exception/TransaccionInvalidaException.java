package service.exception;

public class TransaccionInvalidaException extends RuntimeException {
    public TransaccionInvalidaException(String message) {
        super(message);
    }
}
