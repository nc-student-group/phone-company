package com.phonecompany.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Helper class used to map configuration file to the
 * configuration object
 *
 * @see Config
 * @see Profile
 */
public class DataSourceInfo {

    @JsonProperty("url")
    private String url;

    @JsonProperty("driver-class-name")
    private String driverClassName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("current-schema")
    private String currentSchema;

    public DataSourceInfo() {
    }

    public DataSourceInfo(String url, String driverClassName,
                          String username, String password,
                          String currentSchema) {
        this.url = url;
        this.driverClassName = driverClassName;
        this.username = username;
        this.password = password;
        this.currentSchema = currentSchema;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentSchema() {
        return currentSchema;
    }

    public void setCurrentSchema(String currentSchema) {
        this.currentSchema = currentSchema;
    }

    @Override
    public String toString() {
        return "DataSourceInfo{" +
                "url='" + url + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", currentSchema='" + currentSchema + '\'' +
                '}';
    }
}
