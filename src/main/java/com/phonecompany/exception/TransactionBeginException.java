package com.phonecompany.exception;

import java.sql.SQLException;

public class TransactionBeginException extends RuntimeException {
    public TransactionBeginException(SQLException e) {
        super("Failed to begin transaction.", e);
    }
}
