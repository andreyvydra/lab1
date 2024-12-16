package itmo.is.lab1.services.common.errors;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {
    public HttpStatus httpStatus;

    public GeneralException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
