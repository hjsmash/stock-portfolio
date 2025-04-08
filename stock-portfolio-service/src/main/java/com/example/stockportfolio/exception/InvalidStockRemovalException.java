package com.example.stockportfolio.exception;

public class InvalidStockRemovalException extends RuntimeException {
    public InvalidStockRemovalException(String symbol, int quantity, int removalQuantity) {
        super("Removal Quantity:" + removalQuantity + " is greater than the Available Quantity: " + quantity + " for symbol: " + symbol);
    }
}
