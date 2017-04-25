package com.phonecompany.exception;

import java.sql.SQLException;

public class TransactionRollbackException extends RuntimeException {
    public TransactionRollbackException(SQLException e) {
        super("Failed to rollback transaction.", e);
    }
}
