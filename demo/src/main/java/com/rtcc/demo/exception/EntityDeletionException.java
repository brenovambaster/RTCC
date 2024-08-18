package com.rtcc.demo.exception;

public class EntityDeletionException extends RuntimeException {

    public EntityDeletionException(String entityName, String reason) {
        super(String.format("Erro ao deletar %s: %s", entityName, reason));
    }
}
