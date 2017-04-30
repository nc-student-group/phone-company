package com.phonecompany.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.phonecompany.exception.ConnectionPoolAcquirementException;
import com.phonecompany.model.config.DataSourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import static com.mchange.v2.c3p0.PoolConfig.MAX_IDLE_TIME;

/**
 * A factory for connections to the physical data source that this
 * {@code DbManager} object represents.
 */
public class DbManager {

    private static final Logger LOG = LoggerFactory.getLogger(DbManager.class);
    private static final int MAX_POOL_SIZE = 20; //current heroku postgres limit
    private static final int MAX_IDLE_TIME = 1; // one idle second and connection returns to the pool
    private static final int CHECKOUT_TIMEOUT = 7000;
    private static final int IDLE_CONNECTION_TEST_PERIOD = 100; //allowed number of acquisition attempts
    private static final int MAX_STATEMENTS = 50;
    private static final int MIN_POOL_SIZE = 1;

    private static DbManager dbManager;

    private DataSourceInfo dataSourceInfo;
    private ComboPooledDataSource dataSource;

    /**
     * Sets up the configuration for its further usage during
     * the connection acquirement process
     */
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
            LOG.debug("Setting max pool size to: {}", MAX_POOL_SIZE);
            dataSource.setMaxPoolSize(MAX_POOL_SIZE);
            LOG.debug("Setting min pool size to: {}", MIN_POOL_SIZE);
            dataSource.setMinPoolSize(MIN_POOL_SIZE);
            LOG.debug("Max statements available: {}", MAX_STATEMENTS);
            dataSource.setMaxStatements(MAX_STATEMENTS);
            LOG.debug("Idle test period was set to: {}", IDLE_CONNECTION_TEST_PERIOD);
            dataSource.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_PERIOD);
            LOG.debug("Idle connection time out: {}", CHECKOUT_TIMEOUT);
            dataSource.setCheckoutTimeout(CHECKOUT_TIMEOUT);
            LOG.debug("Setting max idle connection time to: {}", MAX_IDLE_TIME);
            dataSource.setMaxIdleTime(MAX_IDLE_TIME);

            return dataSource;
        } catch (PropertyVetoException e) {
            throw new ConnectionPoolAcquirementException(dataSourceInfo, e);
        }
    }

    /**
     * Attempts to establish a connection with the data source that
     * this {@code DbManager} object represents.
     *
     * @return a connection to the datasource
     * @throws SQLException if connection acquirement fails
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Provides a single instance of the data source this
     * {@code DbManager} object represents.
     *
     * @return fully constructed data source provider object
     */
    public static DbManager getInstance() {
        if(dbManager == null) {
            dbManager = new DbManager();
        }
        return dbManager;
    }
}
