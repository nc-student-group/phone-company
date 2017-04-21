package com.phonecompany.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public abstract class DomainEntity {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Long id;

    public DomainEntity(){}

    public DomainEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DomainEntity{" +
                "id=" + id +
                '}';
    }
}
