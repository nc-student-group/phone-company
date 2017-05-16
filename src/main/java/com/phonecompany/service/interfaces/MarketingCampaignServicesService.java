package com.phonecompany.service.interfaces;

import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.MarketingCampaignServices;

import java.util.List;

public interface MarketingCampaignServicesService extends CrudService<MarketingCampaignServices> {

    List<MarketingCampaignServices> getMarketingCampaignServicesByMarketingCampaign(
            MarketingCampaign campaign);

}
