package com.phonecompany.exception;

import com.phonecompany.model.DomainEntity;

import java.sql.SQLException;

public class EntityPersistenceException extends CrudException {

    public EntityPersistenceException(String message, SQLException cause) {
        super(message, cause);
    }

    public <T extends DomainEntity> EntityPersistenceException(
            T failedEntity, SQLException cause) {
        super("Was unable to persist entity: " + failedEntity, cause);
    }
}
