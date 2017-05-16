package com.phonecompany.dao.interfaces;

import com.phonecompany.model.MarketingCampaignTariff;

import java.util.List;

public interface MarketingCampaignTariffDao extends CrudDao<MarketingCampaignTariff> {

    List<MarketingCampaignTariff> getMarketingCampaignTariffsAvailableForCustomer(Long regionId);
}
