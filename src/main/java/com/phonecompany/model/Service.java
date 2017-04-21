package com.phonecompany.model;

import com.phonecompany.model.enums.ProductStatus;

import javax.validation.constraints.NotNull;

public class Service extends DomainEntity {
    @NotNull(message = "Product category must not be null")
    private ProductCategory productCategory;
    @NotNull(message = "Service name must not be null")
    private String serviceName;
    @NotNull(message = "Price must not be null")
    private double price;
    @NotNull(message = "Product status must not be null")
    private ProductStatus productStatus;
    @NotNull(message = "Discount must not be null")
    private double discount;

    public Service(){}

    public Service(ProductCategory productCategory, String serviceName, double price, ProductStatus productStatus, double discount) {
        this.productCategory = productCategory;
        this.serviceName = serviceName;
        this.price = price;
        this.productStatus = productStatus;
        this.discount = discount;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
