package com.es.phoneshop.exception;

public class OutOfStockException extends IllegalArgumentException {

    public OutOfStockException(int stockRequested, int stockAvailable) {
        super(String.format("Out of stock, available: %d. But requested %d", stockAvailable, stockRequested));
    }

}
