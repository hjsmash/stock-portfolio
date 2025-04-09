package com.example.stockportfolio.exception;

import com.example.stockportfolio.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SPMExceptionHandler {

    // Handle custom application exceptions
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleStockNotFound(StockNotFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("type", "StockNotFoundException");
        errorDetails.put("details", ex.getMessage());

        return new ResponseEntity<>(ApiResponse.error("Stock not found", errorDetails), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidStockRemovalException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidStockRemoval(InvalidStockRemovalException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("type", "InvalidStockRemovalException");
        errorDetails.put("details", ex.getMessage());

        return new ResponseEntity<>(ApiResponse.error("Stock not removed", errorDetails), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VantageApiException.class)
    public ResponseEntity<ApiResponse<?>> handleVantageApiException(VantageApiException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("type", "VantageApiException");
        errorDetails.put("details", ex.getMessage());

        return new ResponseEntity<>(ApiResponse.error("Technical Error from Vantage API", errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle invalid input or validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(ApiResponse.error("Validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    // Handle all other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("type", ex.getClass().getSimpleName());
        errorDetails.put("details", ex.getMessage());

        return new ResponseEntity<>(ApiResponse.error("An unexpected error occurred", errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
