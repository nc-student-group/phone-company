package com.phonecompany.exception.dao_layer;

import com.phonecompany.model.DomainEntity;

import java.sql.SQLException;

public class EntityPersistenceException extends CrudException {

    public <T extends DomainEntity> EntityPersistenceException(
            T failedEntity, SQLException cause) {
        super("Was unable to persist entity: " + failedEntity, cause);
    }
}
