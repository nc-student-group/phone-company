package com.phonecompany.exception;

import javax.mail.MessagingException;
import java.sql.SQLException;

public class TransactionCommitException extends RuntimeException {
    public TransactionCommitException(SQLException e) {
        super("Failed to commit transaction.", e);
    }
}
