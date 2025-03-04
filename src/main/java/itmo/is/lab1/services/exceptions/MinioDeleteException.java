package itmo.is.lab1.services.exceptions;

public class MinioDeleteException extends RuntimeException {
    public MinioDeleteException(String message) {
        super(message);
    }

    public MinioDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}