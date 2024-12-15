package itmo.is.lab1.services.location.errors;

import itmo.is.lab1.services.common.responses.GeneralException;
import org.springframework.http.HttpStatus;

public class LocationNotFoundException extends GeneralException {
    public LocationNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Location не был найден.");
    }
}
