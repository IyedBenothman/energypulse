package com.iyed.energypulse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

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
}