package com.phonecompany.exception;

/**
 * Gets thrown if any conflict occurs.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}