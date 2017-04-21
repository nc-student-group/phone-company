package com.phonecompany.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.phonecompany.exception.ConnectionPoolAcquirementException;
import com.phonecompany.model.config.DataSourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class DbManager {

    private static final Logger LOG = LoggerFactory.getLogger(DbManager.class);

    private static DbManager dbManager;

    private DataSourceInfo dataSourceInfo;
    private ComboPooledDataSource dataSource;

    private DbManager() {
        ConfigManager configManager = ConfigManager.getInstance();
        this.dataSourceInfo = configManager.getDataSourceInfo();
        this.dataSource = this.acquirePooledDataSource();
    }

    private ComboPooledDataSource acquirePooledDataSource() {
        try {
            dataSource = new ComboPooledDataSource();
            LOG.debug("Setting driver class name: {}", dataSourceInfo.getDriverClassName());
            dataSource.setDriverClass(dataSourceInfo.getDriverClassName());
            LOG.debug("Setting username: {}", dataSourceInfo.getUsername());
            dataSource.setUser(dataSourceInfo.getUsername());
            LOG.debug("Setting password: {}", dataSourceInfo.getPassword());
            dataSource.setPassword(dataSourceInfo.getPassword());
            LOG.debug("Setting url: {}", dataSourceInfo.getUrl());
            dataSource.setJdbcUrl(dataSourceInfo.getUrl());

            return dataSource;
        } catch (PropertyVetoException e) {
            throw new ConnectionPoolAcquirementException(dataSourceInfo, e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DbManager getInstance() {
        if(dbManager == null) {
            dbManager = new DbManager();
        }
        return dbManager;
    }
}
