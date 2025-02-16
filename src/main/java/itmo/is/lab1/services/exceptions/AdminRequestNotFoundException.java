package itmo.is.lab1.services.exceptions;

public class AdminRequestNotFoundException extends RuntimeException {
    public AdminRequestNotFoundException(String message) {
        super(message);
    }
}