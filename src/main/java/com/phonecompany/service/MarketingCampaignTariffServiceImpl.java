package com.phonecompany.service;

import com.phonecompany.dao.interfaces.MarketingCampaignTariffDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.MarketingCampaignTariff;
import com.phonecompany.service.interfaces.MarketingCampaignTariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<MarketingCampaignTariff> getMarketingCampaignTariffsAvailableForCustomer(
            Customer customer) {
        if (customer.getCorporate() == null) {
            return marketingCampaignTariffDao.
                    getMarketingCampaignTariffsAvailableForCustomer(customer.getAddress().getRegion().getId());
        } else {
            throw new ConflictException("You are corporate client.");
        }
    }
}
