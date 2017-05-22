package com.phonecompany.dao.interfaces;

import com.phonecompany.model.MarketingCampaign;

import java.util.List;

public interface MarketingCampaignDao extends JdbcOperations<MarketingCampaign> {

    List<MarketingCampaign> getAllByTariffRegion(Long tariffRegionId);

}
