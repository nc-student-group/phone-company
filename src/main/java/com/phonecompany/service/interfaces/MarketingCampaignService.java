package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.MarketingCampaign;

import java.util.List;

public interface MarketingCampaignService extends CrudService<MarketingCampaign> {

    List<MarketingCampaign> getMarketingCampaignsAvailableForCustomer(Customer customer);

    void activateMarketingCampaign(MarketingCampaign campaign, Customer customer);

}
