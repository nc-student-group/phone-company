package com.phonecompany.service;

import com.phonecompany.dao.interfaces.MarketingCampaignTariffDao;
import com.phonecompany.model.MarketingCampaignTariff;
import com.phonecompany.service.interfaces.MarketingCampaignTariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketingCampaignTariffServiceImpl extends CrudServiceImpl<MarketingCampaignTariff>
        implements MarketingCampaignTariffService {

    private MarketingCampaignTariffDao marketingCampaignTariffDao;

    private static final Logger LOG = LoggerFactory.getLogger(MarketingCampaignTariffServiceImpl.class);

    @Autowired
    public MarketingCampaignTariffServiceImpl(MarketingCampaignTariffDao marketingCampaignTariffDao) {
        super(marketingCampaignTariffDao);
        this.marketingCampaignTariffDao = marketingCampaignTariffDao;
    }
}
