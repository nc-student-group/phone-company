package com.phonecompany.dao.interfaces;

import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.MarketingCampaignServices;

import java.util.List;

public interface MarketingCampaignServicesDao extends JdbcOperations<MarketingCampaignServices> {

    List<MarketingCampaignServices> getServicesByMarketingCampaignId(Long mcId);

    MarketingCampaignServices save(MarketingCampaignServices entity, MarketingCampaign marketingCampaign);

}
