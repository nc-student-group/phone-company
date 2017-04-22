package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class ProductCategory extends DomainEntity {

    @NotNull(message = "Category name must not be null")
    private String nameCategory;
    @NotNull(message = "Units must not be null")
    private String units;

    public ProductCategory(){}

    public ProductCategory(String nameCategory, String units) {
        this.nameCategory = nameCategory;
        this.units = units;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
