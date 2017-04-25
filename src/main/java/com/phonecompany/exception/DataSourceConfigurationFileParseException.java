package com.phonecompany.exception;

import java.io.IOException;

public class DataSourceConfigurationFileParseException extends RuntimeException {
    public DataSourceConfigurationFileParseException(String datasourceConfigLocation,
                                                     IOException e) {
        super("Failed to load properties from: " + datasourceConfigLocation, e);
    }
}
