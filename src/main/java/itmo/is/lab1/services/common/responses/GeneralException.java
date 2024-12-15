package itmo.is.lab1.services.common.responses;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {
    public HttpStatus httpStatus;

    public GeneralException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
