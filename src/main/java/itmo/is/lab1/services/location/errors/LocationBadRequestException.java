package itmo.is.lab1.services.location.errors;

import itmo.is.lab1.services.common.responses.GeneralException;
import org.springframework.http.HttpStatus;

public class LocationBadRequestException extends GeneralException {
    public LocationBadRequestException() {
        super(HttpStatus.BAD_REQUEST, "Переданные изменения не валидны.");
    }
}
