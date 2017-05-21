package com.phonecompany.exception.service_layer;

/**
 * Thrown if no results were found for the provided request
 */
public class MissingResultException extends RuntimeException {
    public MissingResultException(String message) {
        super(message);
    }
}
