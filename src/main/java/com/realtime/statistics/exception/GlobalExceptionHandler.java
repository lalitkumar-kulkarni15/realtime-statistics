package com.realtime.statistics.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INVALID_REQUEST = "Invalid request";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();
        populateErrorList(methodArgumentNotValidException, errors);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, INVALID_REQUEST, errors);
        log.error(apiError.getMessage());
        return handleExceptionInternal(methodArgumentNotValidException, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler({TransactionOutOfRangeException.class})
    public ResponseEntity<ApiError> handleTransactionOutOfRangeException(TransactionOutOfRangeException transactionOutOfRangeException) {

        final ApiError apiError = new ApiError(HttpStatus.NO_CONTENT, "Invalid transaction", transactionOutOfRangeException.getMessage());
        log.error(transactionOutOfRangeException.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NO_CONTENT);
    }

    private void populateErrorList(MethodArgumentNotValidException methodArgumentNotValidException, List<String> errors) {

        populateFieldErrors(methodArgumentNotValidException, errors);
        populateObjectErrors(methodArgumentNotValidException, errors);
    }

    private void populateObjectErrors(MethodArgumentNotValidException methodArgumentNotValidException, List<String> errors) {
        for (ObjectError error : methodArgumentNotValidException.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
    }

    private void populateFieldErrors(MethodArgumentNotValidException methodArgumentNotValidException, List<String> errors) {
        for (FieldError error : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
    }
}
