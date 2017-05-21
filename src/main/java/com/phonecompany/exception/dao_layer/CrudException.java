package com.phonecompany.exception.dao_layer;

import java.sql.SQLException;

public class CrudException extends RuntimeException {
    public CrudException(String message, SQLException cause) {
        super(message, cause);
    }
}
