package com.phonecompany.model;

import com.phonecompany.model.enums.ProductStatus;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Service extends DomainEntity {

    @NotNull(message = "Service name must not be null")
    private String serviceName;
    @Min(value = 0, message = "Price must be greater than zero")
    private double price;
    private ProductStatus productStatus = ProductStatus.ACTIVATED; //product is ACTIVATED by default
    @Min(value = 0, message = "Discount should be greater than zero")
    @Max(value = 100, message = "Discount cannot be greater than 100")
    @NotNull(message = "Discount must not be null")
    private double discount;
    @NotNull(message = "Product category must not be null")
    private ProductCategory productCategory;
    private String pictureUrl;
    private String description;
    private String previewDescription;
    @Min(value = 1, message = "This field can only contain non negative integers")
    private int durationInDays;
    @Min(value = 0, message = "This field can only contain non negative integers")
    private int amount;

    public Service() {
    }

    public Service(Long id, String serviceName,
                   Double price, ProductStatus productStatus, Double discount,
                   ProductCategory productCategory, String pictureUrl,
                   String description, String previewDescription,
                   Integer durationInDays, Integer amount) {
        super(id);
        this.serviceName = serviceName;
        this.price = price;
        this.productStatus = productStatus;
        this.discount = discount;
        this.productCategory = productCategory;
        this.pictureUrl = pictureUrl;
        this.description = description;
        this.previewDescription = previewDescription;
        this.durationInDays = durationInDays;
        this.amount = amount;
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

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreviewDescription() {
        return previewDescription;
    }

    public void setPreviewDescription(String previewDescription) {
        this.previewDescription = previewDescription;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceName='" + serviceName + '\'' +
                ", price=" + price +
                ", productStatus=" + productStatus +
                ", discount=" + discount +
                ", productCategory=" + productCategory +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", description='" + description + '\'' +
                ", previewDescription='" + previewDescription + '\'' +
                ", durationInDays=" + durationInDays +
                ", amount=" + amount +
                "} " + super.toString();
    }
}
