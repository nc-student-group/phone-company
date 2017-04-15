package com.phonecompany.exception;

import java.sql.SQLException;

public class CrudException extends RuntimeException {
    public CrudException(String message, SQLException cause) {
        super(message);
    }
}
