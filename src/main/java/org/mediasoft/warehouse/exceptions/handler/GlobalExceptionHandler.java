package org.mediasoft.warehouse.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.exceptions.ErrorDetail;
import org.mediasoft.warehouse.exceptions.SkuIsExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SkuIsExistException.class)
    public ResponseEntity<ErrorDetail> handleSkuIsExistException(SkuIsExistException e) {
        log.error("SKU is exist: {}", e.getMessage());

         final ErrorDetail errorDetail = ErrorDetail
                .builder()
                .exceptionName("SkuIsExistException")
                 .message("SKU is exist:" + e.getMessage())
                .time(LocalDateTime.now())
                .clazz(e.getClass())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException e) {
        log.error("Product with SKU not found: {}", e.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product with SKU not found: " + e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Product with SKU not found: {}", e.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid attribute: " + e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}