package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class TariffRegion extends DomainEntity{
    @NotNull(message = "Region must not be null")
    private Region region;
    @NotNull(message = "Tariff must not be null")
    private Tariff tariff;
    @NotNull(message = "Price must not be null")
    private Double price;

    public TariffRegion(){}

    public TariffRegion(Region region, Tariff tariff, Double price) {
        this.region = region;
        this.tariff = tariff;
        this.price = price;
    }

    public TariffRegion(Long id, Region region, Tariff tariff, Double price) {
        super(id);
        this.region = region;
        this.tariff = tariff;
        this.price = price;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
