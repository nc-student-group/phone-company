package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class MarketingCampaignTariff extends DomainEntity{

    @NotNull(message = "TariffRegion must not be null")
    private TariffRegion tariffRegion;

    public MarketingCampaignTariff() {
    }

    public MarketingCampaignTariff(TariffRegion tariffRegion) {
        this.tariffRegion = tariffRegion;
    }

    public TariffRegion getTariffRegion() { return tariffRegion; }

    public void setTariffRegion(TariffRegion tariffRegion) { this.tariffRegion = tariffRegion; }

    @Override
    public String toString() {
        return "MarketingCampaignTariff{" +
                "tariffRegion=" + tariffRegion +
                '}';
    }
}
