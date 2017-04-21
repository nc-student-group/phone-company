package com.phonecompany.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Profile {

    @JsonProperty("name")
    private String name;

    @JsonProperty("datasource")
    private DataSourceInfo dataSource;

    public Profile() {
    }

    public Profile(String name, DataSourceInfo dataSource) {
        this.name = name;
        this.dataSource = dataSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataSourceInfo getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSourceInfo dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", dataSource=" + dataSource +
                '}';
    }
}
