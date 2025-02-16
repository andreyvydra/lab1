package itmo.is.lab1.services.exceptions;

public class AdminRequestAlreadyProcessedException extends RuntimeException {
    public AdminRequestAlreadyProcessedException(String message) {
        super(message);
    }
}