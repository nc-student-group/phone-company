package com.phonecompany.exception;

import java.sql.SQLException;

public class EntityNotFoundException extends CrudException {
    public EntityNotFoundException(Long entityId, SQLException e) {
        super("Did not manage to extract entity with the given id: " + entityId, e);
    }
}
