package ru.practicum.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final String format = "'%s' -> %s";
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = String.format(format, ((FieldError) error).getRejectedValue(), error.getDefaultMessage());
            errors.put(fieldName, errorMessage);
        });
        final String className = ex.getParameter().getParameterType().getSimpleName();
        log.error("{}: {}", className, errors);
        return errors;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateException(final ValidateException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(final Throwable e) {
        final String messageErr = String.format("An unexpected error has occurred: %s", e.getMessage());
        log.error(messageErr);
        return new ErrorResponse(messageErr);
    }

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<String> handleMissingParameterException(MissingParameterException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}