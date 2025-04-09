package com.example.stockportfolio.exception;

public class VantageApiException extends RuntimeException {
    public VantageApiException(String message) {
        super("Technical Error from Vantage API :" + message);
    }
}
