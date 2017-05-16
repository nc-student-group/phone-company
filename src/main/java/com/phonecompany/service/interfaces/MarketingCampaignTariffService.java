package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.MarketingCampaignTariff;

import java.util.List;

public interface MarketingCampaignTariffService extends CrudService<MarketingCampaignTariff> {

    List<MarketingCampaignTariff> getMarketingCampaignTariffsAvailableForCustomer(
            Customer customer);
}
