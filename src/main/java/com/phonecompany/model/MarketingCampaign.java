package com.phonecompany.model;

import com.phonecompany.model.enums.ProductStatus;

import javax.validation.constraints.NotNull;

public class MarketingCampaign extends DomainEntity {

    @NotNull(message = "Name must not be null")
    private String name;
    @NotNull(message = "MarketingCampaignStatus must not be null")
    private ProductStatus marketingCampaignStatus;
    @NotNull(message = "Description must not be null")
    private String description;

    public MarketingCampaign() {
    }

    public MarketingCampaign(String name, ProductStatus marketingCampaignStatus, String description) {
        this.name = name;
        this.marketingCampaignStatus = marketingCampaignStatus;
        this.description = description;
    }

    public ProductStatus getMarketingCampaignStatus() {  return marketingCampaignStatus; }

    public void setMarketingCampaignStatus(ProductStatus marketingCampaignStatus) { this.marketingCampaignStatus = marketingCampaignStatus; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "MarketingCampaign{" +
                "name='" + name + '\'' +
                ", marketingCampaignStatus=" + marketingCampaignStatus +
                ", description='" + description + '\'' +
                '}';
    }
}
