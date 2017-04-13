package com.phonecompany.exception;

public class CrudException extends RuntimeException {
    public CrudException(String message, Throwable cause) {
        super(message);
    }
}
