package com.phonecompany.exception;

public class EmailAlreadyPresentException extends RuntimeException {

    public EmailAlreadyPresentException(String email) {
        super("User with email " + email + " is already registered");
    }
}
