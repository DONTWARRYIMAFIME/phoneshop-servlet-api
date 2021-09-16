package com.es.phoneshop.exception;

public class IllegalProductQuantityException extends IllegalArgumentException {

    public IllegalProductQuantityException() {
        super("Quantity value must be more than 0");
    }

}
