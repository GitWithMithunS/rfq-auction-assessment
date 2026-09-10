package com.rfq.bid_service.exception;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> error(RuntimeException e) {
        return ResponseEntity.status(e instanceof NoSuchElementException ? 404 : 400).body(Map.of("error", e.getMessage()));
    }
}
