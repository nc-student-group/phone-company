package com.phonecompany.exception;

public class EntityInitializationException extends CrudException {
    public EntityInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
