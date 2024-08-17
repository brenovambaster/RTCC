package com.rtcc.demo.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, String id) {
        super(String.format("%s com ID %s não foi encontrado", entityName, id));
    }
}