package com.example.payments.v0.controllers;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity handleResourceException(ResourceException e) {
        // log exception
        return ResponseEntity.status(e.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Error.builder().error(e.getLocalizedMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        // log exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Error.builder().error(e.getLocalizedMessage()).build());
    }

    @Value
    @Builder
    private static class Error {
        private String error;
    }
}
