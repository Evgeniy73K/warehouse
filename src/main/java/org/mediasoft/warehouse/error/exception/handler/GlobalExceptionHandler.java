package org.mediasoft.warehouse.error.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.PathElementException;
import org.mediasoft.warehouse.error.exception.BusinessException;
import org.mediasoft.warehouse.error.exception.ErrorDetail;
import org.mediasoft.warehouse.error.exception.ProductValidationException;
import org.mediasoft.warehouse.error.exception.SkuIsExistException;
import org.mediasoft.warehouse.error.exception.UserNotAvailableException;
import org.mediasoft.warehouse.error.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mediasoft.warehouse.error.code.ValidationExceptionMessage.NOT_ENOUGH_QTY;
import static org.mediasoft.warehouse.error.code.ValidationExceptionMessage.PRODUCT_NOT_AVAILABLE;
import static org.mediasoft.warehouse.error.code.ValidationExceptionMessage.PRODUCT_NOT_FOUND;
import static org.mediasoft.warehouse.error.code.ValidationExceptionMessage.USER_NOT_AVAILABLE;
import static org.mediasoft.warehouse.error.code.ValidationExceptionMessage.USER_NOT_FOUND;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SkuIsExistException.class)
    public ResponseEntity<ErrorDetail> handleSkuIsExistException(SkuIsExistException e) {
        log.error("SKU is exist: {} by product with id: {}", e.getMessage(), e.getProductId());

        final ErrorDetail errorDetail = ErrorDetail
                .builder()
                .exceptionName("SkuIsExistException")
                .message("SKU is exist: " + e.getMessage() + " by pid:" + e.getProductId())
                .time(LocalDateTime.now())
                .clazz(e.getClass())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Product with SKU not found: {}", e.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid attribute: " + e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PathElementException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentPathElementException(PathElementException e) {
        log.error("Invalid attribute: {}", e.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid attribute: " + e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetail> handeUserNotFoundException(UserNotFoundException e) {
        log.error("{}{}", USER_NOT_FOUND.getMessage(), e.getUserId());

        final ErrorDetail errorDetail = ErrorDetail
                .builder()
                .exceptionName(e.getClass().getSimpleName())
                .message(e.getMessage() + e.getUserId())
                .time(LocalDateTime.now())
                .clazz(e.getClass())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(UserNotAvailableException.class)
    public ResponseEntity<ErrorDetail> handeUserNotAvailableException(UserNotAvailableException e) {
        log.error("{}{}", USER_NOT_AVAILABLE.getMessage(), e.getUserId());

        final ErrorDetail errorDetail = ErrorDetail
                .builder()
                .exceptionName(e.getClass().getSimpleName())
                .message(e.getMessage() + e.getUserId())
                .time(LocalDateTime.now())
                .clazz(e.getClass())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<List<ErrorDetail>> handleProductValidationException(ProductValidationException e) {

        var productNotFoundError = addErrorDetail(e.getNotFoundProducts(), e, PRODUCT_NOT_FOUND.getMessage());
        var productNotAvailableError = addErrorDetail(e.getUnAvailableProducts(), e, PRODUCT_NOT_AVAILABLE.getMessage());
        var notEnoughQtyError = addErrorDetailFromMap(e.getNotEnoughProductMap(), e, NOT_ENOUGH_QTY.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of(productNotFoundError, productNotAvailableError, notEnoughQtyError));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(BusinessException e) {
        log.error("BusinessException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    private ErrorDetail addErrorDetail(Collection<?> items, ProductValidationException e, String errorMessage) {
        return ErrorDetail
                .builder()
                .exceptionName(e.getClass().getSimpleName())
                .message(String.format("%s%s", errorMessage, items))
                .time(LocalDateTime.now())
                .clazz(e.getClass())
                .build();

    }

    private ErrorDetail addErrorDetailFromMap(Map<?, ?> map, ProductValidationException e, String errorMessage) {
        return ErrorDetail
                .builder()
                .exceptionName(e.getClass().getSimpleName())
                .message(String.format("%s%s", errorMessage, map))
                .time(LocalDateTime.now())
                .clazz(e.getClass())
                .build();
    }
}