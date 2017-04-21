package com.phonecompany.exception;

public class ConnectionPoolAcquirementException
        extends DataSourceConfigurationException {
    public ConnectionPoolAcquirementException(Object configuration, Throwable cause) {
        super("Where not able to establish a connection pool with " +
                "the following configuration: " + configuration, cause);
    }
}
