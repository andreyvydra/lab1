package itmo.is.lab1.services.location.errors;

import itmo.is.lab1.services.common.responses.GeneralException;
import org.springframework.http.HttpStatus;

public class LocationForbiddenException extends GeneralException {
    public LocationForbiddenException() {
        super(HttpStatus.FORBIDDEN, "Location не доступен.");
    }
}
