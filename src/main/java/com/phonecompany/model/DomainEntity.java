package com.phonecompany.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phonecompany.model.enums.interfaces.Storable;

public abstract class DomainEntity implements Storable {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Long id;

    public DomainEntity(){}

    public DomainEntity(Long id) {
        this.id = id;
    }

    @Override
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
