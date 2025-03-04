package itmo.is.lab1.services.exceptions;

public class MinioUploadException extends RuntimeException {
    public MinioUploadException(String message) {
        super(message);
    }

    public MinioUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}