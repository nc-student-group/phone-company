package com.phonecompany.exception.dao_layer;

import java.sql.SQLException;

public class EntityDeletionException extends CrudException {
    public EntityDeletionException(Long id, SQLException e) {
        super("Failed to delete entity with an id: " + id, e);
    }
}
