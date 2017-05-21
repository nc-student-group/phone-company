package com.phonecompany.dao.interfaces;

import com.phonecompany.model.MarketingCampaignTariff;

import java.util.List;

public interface MarketingCampaignTariffDao extends JdbcOperations<MarketingCampaignTariff> {

    List<MarketingCampaignTariff> getMarketingCampaignTariffsAvailableForCustomer(Long regionId);
}
