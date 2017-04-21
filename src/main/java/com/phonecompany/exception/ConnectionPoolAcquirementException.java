package com.phonecompany.exception;

import com.phonecompany.model.config.DataSourceInfo;

public class ConnectionPoolAcquirementException
        extends DataSourceConfigurationException {
    public ConnectionPoolAcquirementException(DataSourceInfo configuration,
                                              Throwable cause) {
        super("Where not able to establish a connection pool with " +
                "the following configuration: " + configuration, cause);
    }
}
