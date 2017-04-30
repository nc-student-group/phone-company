package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class MarketingCampaignServices extends DomainEntity{

    @NotNull(message = "MarketingCampaign must not be null")
    private MarketingCampaign marketingCampaign;
    @NotNull(message = "Service must not be null")
    private Service service;
    @NotNull(message = "Price must not be null")
    private double price;

    public MarketingCampaignServices() {
    }

    public MarketingCampaignServices(MarketingCampaign marketingCampaign, Service service, double price) {
        this.marketingCampaign = marketingCampaign;
        this.service = service;
        this.price = price;
    }

    public MarketingCampaign getMarketingCampaign() { return marketingCampaign; }

    public void setMarketingCampaign(MarketingCampaign marketingCampaign) { this.marketingCampaign = marketingCampaign; }

    public Service getService() { return service; }

    public void setService(Service service) { this.service = service; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "MarketingCampaignServices{" +
                "marketingCampaign=" + marketingCampaign +
                ", service=" + service +
                ", price=" + price +
                '}';
    }
}
