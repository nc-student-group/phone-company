package com.phonecompany.dao.interfaces;

import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.enums.ProductStatus;

import java.util.List;

public interface MarketingCampaignDao extends JdbcOperations<MarketingCampaign> {

    List<MarketingCampaign> getAllByTariffRegion(Long tariffRegionId);

    void updateMarketingCampaignStatus(Long campaignId, ProductStatus productStatus);

}
