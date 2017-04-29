package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class MarketingCampaignTariff extends DomainEntity{
    @NotNull(message = "TariffRegion must not be null")
    private TariffRegion tariffRegion;
    @NotNull(message = "MarketingCampaign must not be null")
    private MarketingCampaign marketingCampaign;

    public MarketingCampaignTariff() {
    }

    public MarketingCampaignTariff(TariffRegion tariffRegion, MarketingCampaign marketingCampaign) {
        this.tariffRegion = tariffRegion;
        this.marketingCampaign = marketingCampaign;
    }

    public TariffRegion getTariffRegion() { return tariffRegion; }

    public void setTariffRegion(TariffRegion tariffRegion) { this.tariffRegion = tariffRegion; }

    public MarketingCampaign getMarketingCampaign() { return marketingCampaign; }

    public void setMarketingCampaign(MarketingCampaign marketingCampaign) { this.marketingCampaign = marketingCampaign; }

    @Override
    public String toString() {
        return "MarketingCampaignTariff{" +
                "tariffRegion=" + tariffRegion +
                ", marketingCampaign=" + marketingCampaign +
                '}';
    }
}
