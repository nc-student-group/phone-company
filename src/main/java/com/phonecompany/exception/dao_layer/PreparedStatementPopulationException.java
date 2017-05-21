package com.phonecompany.exception.dao_layer;

import java.sql.SQLException;

public class PreparedStatementPopulationException extends RuntimeException {

    public PreparedStatementPopulationException(SQLException e) {
        super("Failed to populate PreparedStatement instance. " +
                "There may be more or less actual parameters in " +
                "the prepared statement than you are trying to populate.", e);
    }
}
