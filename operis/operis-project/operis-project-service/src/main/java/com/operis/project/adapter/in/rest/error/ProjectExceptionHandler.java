package com.operis.project.adapter.in.rest.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// exception global handler for REST controllers

@RestControllerAdvice
@Slf4j
public class ProjectExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> fieldErrors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrors.add(
               "%s: %s".formatted(error.getField(), error.getDefaultMessage())
        ));

        ApiError body = new ApiError(
                status.value(),
                HttpStatus.valueOf(status.value()).name(),
                "Validation failed for one or more arguments.",
                fieldErrors
        );

        return handleExceptionInternal(ex, body, headers, status, request);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
//        List<String> details = new ArrayList<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error -> details.add(
//               "%s: %s".formatted(error.getField(), error.getDefaultMessage())
//        ));
//
//        ApiError apiError = new ApiError(
//                HttpStatus.BAD_REQUEST.value(),
//                HttpStatus.BAD_REQUEST.name(),
//                "Validation failed for one or more arguments.",
//                details
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(MissingRequestHeaderException.class)
//    public ResponseEntity<ApiError> handleMissingRequestHeaderException(MissingRequestHeaderException ex, WebRequest request) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage()));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error("An exception occurred: ", ex);

        if(!(body instanceof ApiError)) {
            body = new ApiError(
                    statusCode.value(),
                    HttpStatus.valueOf(statusCode.value()).name(),
                    ex.getMessage()
            );
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
}
