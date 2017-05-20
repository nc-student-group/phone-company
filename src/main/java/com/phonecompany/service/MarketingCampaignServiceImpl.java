package com.phonecompany.service;

import com.phonecompany.dao.interfaces.MarketingCampaignDao;
import com.phonecompany.dao.interfaces.MarketingCampaignTariffDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.MarketingCampaignTariff;
import com.phonecompany.service.interfaces.MarketingCampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketingCampaignServiceImpl extends CrudServiceImpl<MarketingCampaign>
        implements MarketingCampaignService {

    private MarketingCampaignTariffDao marketingCampaignTariffDao;
    private MarketingCampaignDao marketingCampaignDao;

    private static final Logger LOG = LoggerFactory.getLogger(MarketingCampaignServiceImpl.class);

    @Autowired
    public MarketingCampaignServiceImpl(MarketingCampaignDao marketingCampaignDao,
                                        MarketingCampaignTariffDao marketingCampaignTariffDao) {
        super(marketingCampaignDao);
        this.marketingCampaignDao = marketingCampaignDao;
        this.marketingCampaignTariffDao = marketingCampaignTariffDao;
    }

    @Override
    public List<MarketingCampaign> getMarketingCampaignsAvailableForCustomer(Customer customer) {
        List<MarketingCampaign> campaigns = new ArrayList<>();
        LOG.info("Retrieving available marketing campaigns for customer with id = "
                + customer.getId());
        if (customer.getCorporate() == null) {
            List<MarketingCampaignTariff> tariffs = marketingCampaignTariffDao.
                    getMarketingCampaignTariffsAvailableForCustomer(customer
                            .getAddress().getRegion().getId());
            if (tariffs != null) {
                for (MarketingCampaignTariff tariff: tariffs) {
                    campaigns.addAll(marketingCampaignDao.getAllByMarketingTariff(tariff.getId()));
                }
            }
        } else {
            throw new ConflictException("You are corporate client.");
        }
        return campaigns;
    }
}
