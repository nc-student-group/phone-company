package com.phonecompany.exception;

import java.sql.SQLException;

public class TransactionCommitException extends RuntimeException {
    public TransactionCommitException(SQLException e) {
        super("Failed to commit transaction.", e);
    }
}
