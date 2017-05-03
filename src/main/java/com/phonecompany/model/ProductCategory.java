package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class ProductCategory extends DomainEntity {

    @NotNull(message = "Category name must not be null")
    private String categoryName;
    @NotNull(message = "Units must not be null")
    private String units;

    public ProductCategory() {
    }

    public ProductCategory(Long id, String categoryName, String units) {
        super(id);
        this.categoryName = categoryName;
        this.units = units;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "categoryName='" + categoryName + '\'' +
                ", units='" + units + '\'' +
                "} " + super.toString();
    }
}
