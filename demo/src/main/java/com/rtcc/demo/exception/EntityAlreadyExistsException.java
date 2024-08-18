package com.rtcc.demo.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String entityName) {
        super(String.format("%s já existe", entityName));
    }
}
