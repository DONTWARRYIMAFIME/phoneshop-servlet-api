package com.es.phoneshop.exception;

public class EntityNotFoundException extends IllegalArgumentException {

    public EntityNotFoundException(String entity, Long id) {
        super(entity + " with ID: " + id + " not found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}
