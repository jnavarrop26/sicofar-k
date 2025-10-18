package service.exception;

public class BodegaCapacityExceededException extends RuntimeException {
    public BodegaCapacityExceededException(String message) {
        super(message);
    }
}
