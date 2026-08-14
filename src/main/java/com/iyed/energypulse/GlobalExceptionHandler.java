package com.iyed.energypulse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException exception) {
        
        Map<String, Object> body = Map.of(
            "status", exception.getStatusCode().value(),
            "message", exception.getReason()
        );

        return ResponseEntity
            .status(exception.getStatusCode())
            .body(body);

        }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException exception){
        
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : exception.getBindingResult().getFieldErrors()){
            errors.put(
                error.getField(),
                error.getDefaultMessage()
            );
        }

        Map<String, Object> body = Map.of(
            "status", 400,
            "message", "Validation failed",
            "errors", errors
        );

        return ResponseEntity
                .badRequest()
                .body(body);
    }

}