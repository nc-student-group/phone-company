package com.phonecompany.exception.dao_layer;

import java.sql.SQLException;

public class EntityInitializationException extends CrudException {
    public EntityInitializationException(SQLException cause) {
        super("Were unable to initialize entity. " +
                "Probably you are trying to extract " +
                "value that is not present in the result set. " +
                "Please, check your init() method", cause);
    }
}
