package com.phonecompany.model;

import com.phonecompany.model.enums.ProductStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MarketingCampaign extends DomainEntity {

    @NotNull(message = "Name must not be null")
    private String name;
    @NotNull(message = "MarketingCampaignStatus must not be null")
    private ProductStatus marketingCampaignStatus;
    @NotNull(message = "Description must not be null")
    private String description;
    @NotNull(message = "Marketing campaign tariff must not be null")
    private MarketingCampaignTariff campaignTariff;
    @NotNull(message = "Marketing campaign services must not be null")
    private List<MarketingCampaignServices> services;


    public MarketingCampaign() {
    }

    public MarketingCampaign(String name, ProductStatus marketingCampaignStatus, String description,
                             MarketingCampaignTariff campaignTariff, List<MarketingCampaignServices> services) {
        this.name = name;
        this.marketingCampaignStatus = marketingCampaignStatus;
        this.description = description;
        this.campaignTariff = campaignTariff;
        this.services = services;
    }

    public ProductStatus getMarketingCampaignStatus() {  return marketingCampaignStatus; }

    public void setMarketingCampaignStatus(ProductStatus marketingCampaignStatus) { this.marketingCampaignStatus = marketingCampaignStatus; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public MarketingCampaignTariff getCampaignTariff() {
        return campaignTariff;
    }

    public void setCampaignTariff(MarketingCampaignTariff campaignTariff) {
        this.campaignTariff = campaignTariff;
    }

    public List<MarketingCampaignServices> getServices() {
        return services;
    }

    public void setServices(List<MarketingCampaignServices> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "MarketingCampaign{" +
                "name='" + name + '\'' +
                ", marketingCampaignStatus=" + marketingCampaignStatus +
                ", description='" + description + '\'' +
                ", marketingCampaignTariff='" + campaignTariff + '\'' +
                ", marketingCampaignServices='" + services + '\'' +
                '}';
    }
}
