package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.enums.ProductStatus;

import java.util.List;
import java.util.Map;

public interface MarketingCampaignService extends CrudService<MarketingCampaign> {

    List<MarketingCampaign> getMarketingCampaignsAvailableForCustomer(Customer customer);

    void activateMarketingCampaign(MarketingCampaign campaign, Customer customer);

    Map<String, Object> getMarketingCampaignsTable(int page, int size);

    void updateMarketingCampaignStatus(Long campaignId, ProductStatus productStatus);

}
