package com.phonecompany.exception;

public class ServiceAlreadyPresentException extends RuntimeException {
    public ServiceAlreadyPresentException(String serviceName) {
        super("Service with name " + serviceName + " already exists");
    }
}
