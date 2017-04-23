package com.phonecompany.exception;

import com.phonecompany.model.DomainEntity;

import java.sql.SQLException;

public class EntityModificationException extends RuntimeException {
    public <T extends DomainEntity> EntityModificationException(T entity, SQLException cause) {
        super("Were unable to modify entity: " + entity, cause);
    }

    public <T extends DomainEntity> EntityModificationException(Long entity, SQLException cause) {
        super("Were unable to modify entity: " + entity, cause);
    }
}
