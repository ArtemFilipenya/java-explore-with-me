package ru.practicum.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        String msg;
        if (cause instanceof ValueInstantiationException) {
            ValueInstantiationException vie = (ValueInstantiationException) cause;
            msg = vie.getOriginalMessage();
        } else {
            msg = e.getMessage();
        }
        log.debug("Http message not readable exception", e);
        return ApiError.builder()
                .reason("Http message not readable exception")
                .status(BAD_REQUEST.toString())
                .message(msg)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handlePropertyValueException(final PropertyValueException e) {
        log.debug("Property value exception", e);
        return ApiError.builder()
                .reason("Property value exception")
                .status(BAD_REQUEST.toString())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String format = "Field: '%s'. Error: %s.";
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = String.format(format, ((FieldError) error).getRejectedValue(), error.getDefaultMessage());
            errors.put(fieldName, errorMessage);
        });
        log.debug("Incorrectly made request.", e);
        return ApiError.builder()
                .reason("Incorrectly made request.")
                .status(BAD_REQUEST.toString())
                .message(errors.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        Throwable cause = e.getCause();
        String msg;
        if (cause instanceof ConversionFailedException) {
            ConversionFailedException cfe = (ConversionFailedException) cause;
            Throwable mostSpecificCause = cfe.getMostSpecificCause();
            msg = mostSpecificCause.getMessage();
        } else msg = e.getMessage();
        log.debug("Method argument type mismatch", e);
        return ApiError.builder()
                .reason("Method argument type mismatch")
                .status(BAD_REQUEST.toString())
                .message(msg)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.debug("Missing servlet request parameter exception", e);
        return ApiError.builder()
                .reason("Missing servlet request parameter exception")
                .status(BAD_REQUEST.toString())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.debug("Required request parameter", e);
        return ApiError.builder()
                .message("Required request parameter")
                .status(BAD_REQUEST.toString())
                .reason(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.debug("Constraint violation exception", e);
        return ApiError.builder()
                .reason("Constraint violation exception")
                .status(BAD_REQUEST.toString())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.debug("Not found exception", e);
        return getApiError(e.getApiError(), NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.debug("Conflict exception", e);
        return getApiError(e.getApiError(), CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleValidateException(final ValidateException e) {
        log.debug("Validate exception", e);
        return getApiError(e.getApiError(), BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleResponseException(final ResponseException e) {
        log.debug("Response exception", e);
        return getApiError(e.getApiError(), BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleInvalidFormatException(final InvalidFormatException e) {
        log.debug("Illegal argument exception", e);
        return ApiError.builder()
                .reason("Illegal argument exception")
                .status(BAD_REQUEST.name())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerError(final Throwable e) {
        log.debug("An unexpected error has occurred", e);
        return ApiError.builder()
                .reason("An unexpected error has occurred")
                .status(INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(Object::toString)
                        .collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ApiError getApiError(ApiError e, HttpStatus conflict) {
        return e.toBuilder()
                .status(conflict.name())
                .build();
    }
}
