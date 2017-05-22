package com.phonecompany.service;

import com.phonecompany.dao.interfaces.MarketingCampaignDao;
import com.phonecompany.dao.interfaces.MarketingCampaignTariffDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.interfaces.CustomerServiceService;
import com.phonecompany.service.interfaces.MarketingCampaignService;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.service.interfaces.TariffService;
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
    private TariffService tariffService;
    private CustomerServiceService customerServiceService;
    private OrderService orderService;

    private static final Logger LOG = LoggerFactory.getLogger(MarketingCampaignServiceImpl.class);

    @Autowired
    public MarketingCampaignServiceImpl(MarketingCampaignDao marketingCampaignDao,
                                        MarketingCampaignTariffDao marketingCampaignTariffDao,
                                        TariffService tariffService,
                                        CustomerServiceService customerServiceService,
                                        OrderService orderService) {
        this.marketingCampaignDao = marketingCampaignDao;
        this.marketingCampaignTariffDao = marketingCampaignTariffDao;
        this.tariffService = tariffService;
        this.customerServiceService = customerServiceService;
        this.orderService = orderService;
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

    @Override
    public void activateMarketingCampaign(MarketingCampaign campaign, Customer customer) {
        LOG.info("Trying to activate marketing campaign for customer with id = "
                + customer.getId());
        for (MarketingCampaignServices service: campaign.getServices()) {
            CustomerServiceDto activatedCustomerService =
                    customerServiceService.activateMarketingServiceForCustomer(service, customer);
            this.orderService.saveCustomerServiceOrder(
                    activatedCustomerService, OrderType.ACTIVATION);
        }
        Long tariffId = campaign.getCampaignTariff()
                .getTariffRegion().getTariff().getId();
        tariffService.activateTariffForSingleCustomer(tariffId, customer);
    }
}
