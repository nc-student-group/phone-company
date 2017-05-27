package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class MarketingCampaignServices extends DomainEntity {

    @NotNull(message = "Service must not be null")
    private Service service;
    @NotNull(message = "Price must not be null")
    private double price;

    public MarketingCampaignServices() {
    }

    public MarketingCampaignServices(Service service, double price) {
        this.service = service;
        this.price = price;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MarketingCampaignServices{" +
                "service=" + service +
                ", price=" + price +
                '}';
    }
}
