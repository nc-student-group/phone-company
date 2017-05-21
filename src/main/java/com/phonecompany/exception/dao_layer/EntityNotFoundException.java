package com.phonecompany.exception.dao_layer;

import com.phonecompany.exception.dao_layer.CrudException;

import java.sql.SQLException;

public class EntityNotFoundException extends CrudException {
    public EntityNotFoundException(Long entityId, SQLException e) {
        super("Did not manage to extract entity with the given id: " + entityId, e);
    }

    public EntityNotFoundException(String identifier, SQLException e) {
        super("Did not manage to extract entity " +
                "with the given identifier: " + identifier, e);
    }
}
