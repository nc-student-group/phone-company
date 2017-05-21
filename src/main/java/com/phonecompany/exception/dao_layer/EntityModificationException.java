package com.phonecompany.exception.dao_layer;

import com.phonecompany.model.DomainEntity;

import java.sql.SQLException;

public class EntityModificationException extends RuntimeException {

    public <T extends DomainEntity> EntityModificationException(T entity, SQLException cause) {
        super("Were unable to modify entity: " + entity, cause);
    }

    public EntityModificationException(Long id, SQLException cause) {
        super("Were unable to modify entity with an id: " + id, cause);
    }

    public EntityModificationException(SQLException cause) {
        super("Were unable to modify entity", cause);
    }
}
