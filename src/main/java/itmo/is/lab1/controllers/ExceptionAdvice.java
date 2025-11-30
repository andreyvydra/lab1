package itmo.is.lab1.controllers;

import itmo.is.lab1.services.common.errors.GeneralException;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import itmo.is.lab1.services.common.responses.GeneralResponse;
import itmo.is.lab1.services.storage.StorageUnavailableException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLTransientConnectionException;
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
        StringBuilder message = new StringBuilder("Нарушены ограничения валидации:");
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message.append(String.format("\nПоле '%s': %s", violation.getPropertyPath(), violation.getMessage()));
        }
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage(message.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResponse> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder("Ошибка валидации входных данных:");
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GeneralResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        String message = "Нарушение целостности данных: возможно, запись с такими уникальными полями уже существует.";
        Throwable cause = e.getMostSpecificCause();
        if (cause != null && cause.getMessage() != null && cause.getMessage().contains("person_passportid_key")) {
            message = "Нарушение целостности данных: паспорт с таким номером уже существует.";
        }
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage(message);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<GeneralResponse> handleTransactionSystemException(TransactionSystemException e) {
        GeneralMessageResponse response = new GeneralMessageResponse()
                .setMessage("Ошибка выполнения транзакции: операция прервана, попробуйте повторить позже.");
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GeneralResponse> handleUsernameException(UsernameNotFoundException e) {
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(StorageUnavailableException.class)
    public ResponseEntity<GeneralResponse> handleStorageUnavailable(StorageUnavailableException e) {
        GeneralMessageResponse response = new GeneralMessageResponse().setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({
            CannotCreateTransactionException.class,
            JDBCConnectionException.class,
            SQLTransientConnectionException.class
    })
    public ResponseEntity<GeneralResponse> handleDatabaseUnavailable(Exception e) {
        GeneralMessageResponse response = new GeneralMessageResponse()
                .setMessage("Сервис базы данных недоступен");
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

}