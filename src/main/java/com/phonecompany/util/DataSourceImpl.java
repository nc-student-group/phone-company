package com.phonecompany.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.phonecompany.exception.ConnectionPoolAcquirementException;
import com.phonecompany.model.config.DataSourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A factory for connections to the physical data source that this
 * {@code DataSource} object represents.
 */
@Component
public class DataSourceImpl implements DataSource {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceImpl.class);
    private static final int MAX_POOL_SIZE = 20;
    private static final int MAX_IDLE_TIME = 1; // one idle second and connection returns to the pool
    private static final int CHECKOUT_TIMEOUT = 0;
    private static final int IDLE_CONNECTION_TEST_PERIOD = 5; //allowed number of acquisition attempts
    private static final int MAX_STATEMENTS = 50;
    private static final int MIN_POOL_SIZE = 1;
    private static final int ACQUIRE_RETRY_ATTEMPTS = 10;
    private static final int MAX_CONNECTION_AGE = 1800;

    private DataSourceInfo dataSourceInfo;
    private ComboPooledDataSource dataSource;

    /**
     * Sets up the configuration for its further usage during
     * the connection acquirement process
     */
    @PostConstruct
    public void init() {
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
            LOG.debug("Setting min pool size to: {}", MIN_POOL_SIZE);
            dataSource.setMinPoolSize(MIN_POOL_SIZE);
            LOG.debug("Setting max pool size to: {}", MAX_POOL_SIZE);
            dataSource.setMaxPoolSize(MAX_POOL_SIZE);
            LOG.debug("Max statements available: {}", MAX_STATEMENTS);
            dataSource.setMaxStatements(MAX_STATEMENTS);
            LOG.debug("Idle test period was set to: {}", IDLE_CONNECTION_TEST_PERIOD);
            dataSource.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_PERIOD);
            LOG.debug("Idle connection time out: {}", CHECKOUT_TIMEOUT);
            dataSource.setCheckoutTimeout(CHECKOUT_TIMEOUT);
            LOG.debug("Setting max idle connection time to: {}", MAX_IDLE_TIME);
            dataSource.setMaxIdleTime(MAX_IDLE_TIME);
            LOG.debug("Setting retry attempts to : {}", ACQUIRE_RETRY_ATTEMPTS);
            dataSource.setAcquireRetryAttempts(ACQUIRE_RETRY_ATTEMPTS);
            LOG.debug("Setting max connection age to: {}", MAX_CONNECTION_AGE);
            dataSource.setMaxConnectionAge(MAX_CONNECTION_AGE);
            dataSource.setTestConnectionOnCheckin(false);

            return dataSource;
        } catch (PropertyVetoException e) {
            throw new ConnectionPoolAcquirementException(dataSourceInfo, e);
        }
    }

    /**
     * Attempts to establish a connection with the data source that
     * this {@code DataSource} object represents.
     *
     * @return a connection to the datasource
     * @throws SQLException if connection acquirement fails
     */
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
