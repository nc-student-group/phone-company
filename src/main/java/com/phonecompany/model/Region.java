package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class Region extends DomainEntity {
    @NotNull(message = "Region name must not be null")
    private String nameRegion;

    public Region(){}

    public Region(String nameRegion) {
        this.nameRegion = nameRegion;
    }

    public String getNameRegion() {
        return nameRegion;
    }

    public void setNameRegion(String nameRegion) {
        this.nameRegion = nameRegion;
    }
}
