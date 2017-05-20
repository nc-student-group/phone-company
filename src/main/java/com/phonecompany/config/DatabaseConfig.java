package com.phonecompany.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.beans.PropertyVetoException;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String CONN_STR;
    @Value("${spring.datasource.driver-class-name}")
    private String DRIVER_CLASS;
    @Value("${spring.datasource.tomcat.max-active}")
    private int MAX_ACTIVE;

    @Bean
    public ShaPasswordEncoder shaPasswordEncoder() {
        return new ShaPasswordEncoder();
    }

    @Bean
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource ComboPooledDataSource = new ComboPooledDataSource();
        ComboPooledDataSource.setDriverClass(DRIVER_CLASS);
        ComboPooledDataSource.setJdbcUrl(CONN_STR);
        ComboPooledDataSource.setMaxPoolSize(MAX_ACTIVE);
        return ComboPooledDataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() throws PropertyVetoException {
        return new DataSourceTransactionManager(dataSource());
    }
}
