package itmo.is.lab1.services.common.errors;

import org.springframework.http.HttpStatus;

public class BadRequestException extends GeneralException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, "Переданные изменения не валидны.");
    }
}
