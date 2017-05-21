package com.phonecompany.exception.service_layer;

public class TypeNotSupportedException extends RuntimeException {

    public TypeNotSupportedException(Class<?> unsupportedType) {
        this("The only supported classes are: instances of Number, String and Date. " +
                "But you provided: " + unsupportedType.getSimpleName());
    }

    public TypeNotSupportedException(String message) {
        super(message);
    }
}
