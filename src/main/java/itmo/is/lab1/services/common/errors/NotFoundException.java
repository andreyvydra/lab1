package itmo.is.lab1.services.common.errors;

import org.springframework.http.HttpStatus;

public class NotFoundException extends GeneralException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, "Элемент не был найден.");
    }
}
