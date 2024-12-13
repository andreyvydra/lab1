package itmo.is.lab1.controllers;

import itmo.is.lab1.services.common.responses.GeneralException;
import itmo.is.lab1.services.common.responses.GeneralResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<GeneralResponse> handleException(GeneralException e) {
        GeneralResponse response = new GeneralResponse().setMessage(e.getMessage());
        return new ResponseEntity<>(response, e.httpStatus);
    }

}
