package com.phonecompany.exception.service_layer;

public class KeyAlreadyPresentException extends RuntimeException {
    public KeyAlreadyPresentException(String key) {
        super("User associated with " + key + " already exists");
    }
}
