package itmo.is.lab1.controllers;

import itmo.is.lab1.services.common.errors.GeneralException;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import itmo.is.lab1.services.common.responses.GeneralResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<GeneralResponse> handleException(GeneralException e) {
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage(e.getMessage());
        return new ResponseEntity<>(response, e.httpStatus);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GeneralResponse> handleConstraintException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder("Нарушены ограничения:");
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message.append(String.format("\nПоле '%s': %s", violation.getPropertyPath(), violation.getMessage()));
        }
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage(message.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResponse> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder("Невалидные значения аргументов:");
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            message.append(String.format("\nПоле '%s': %s", fieldError.getField(), fieldError.getDefaultMessage()));
        }
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage(message.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GeneralResponse> AuthException(AuthenticationException e) {
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage("Неверный логин или пароль!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GeneralResponse> handleUsernameException(ConstraintViolationException e) {
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
