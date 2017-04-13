package com.phonecompany.exception;

public class EntityNotFoundException extends CrudException {
    public EntityNotFoundException(Long entityId, Throwable e) {
        super("Did not manage to extract entity with the given id: " + entityId, e);
    }
}
