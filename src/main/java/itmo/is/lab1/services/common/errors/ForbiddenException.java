package itmo.is.lab1.services.common.errors;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends GeneralException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "Отказано в доступе.");
    }
}
