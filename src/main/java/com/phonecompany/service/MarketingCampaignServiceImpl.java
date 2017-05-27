package com.phonecompany.service;

import com.phonecompany.dao.interfaces.MarketingCampaignDao;
import com.phonecompany.dao.interfaces.MarketingCampaignServicesDao;
import com.phonecompany.dao.interfaces.TariffRegionDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.CustomerServiceService;
import com.phonecompany.service.interfaces.MarketingCampaignService;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.service.interfaces.TariffService;
import com.phonecompany.util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarketingCampaignServiceImpl extends CrudServiceImpl<MarketingCampaign>
        implements MarketingCampaignService {

    private TariffRegionDao tariffRegionDao;
    private MarketingCampaignDao marketingCampaignDao;
    private MarketingCampaignServicesDao marketingCampaignServicesDao;
    private TariffService tariffService;
    private CustomerServiceService customerServiceService;
    private OrderService orderService;

    private static final Logger LOG = LoggerFactory.getLogger(MarketingCampaignServiceImpl.class);

    @Autowired
    public MarketingCampaignServiceImpl(MarketingCampaignDao marketingCampaignDao,
                                        MarketingCampaignServicesDao marketingCampaignServicesDao,
                                        TariffRegionDao tariffRegionDao,
                                        TariffService tariffService,
                                        CustomerServiceService customerServiceService,
                                        OrderService orderService) {
        this.marketingCampaignDao = marketingCampaignDao;
        this.marketingCampaignServicesDao = marketingCampaignServicesDao;
        this.tariffRegionDao = tariffRegionDao;
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
            List<TariffRegion> tariffs = tariffRegionDao.
                    getAllByRegionId(customer.getAddress().getRegion().getId());
            if (tariffs != null) {
                for (TariffRegion tariff: tariffs) {
                    campaigns.addAll(marketingCampaignDao.getAllByTariffRegion(tariff.getId()));
                }
            }
        } else {
            LOG.warn("Corporate client can not see marketing campaigns");
            return Collections.emptyList();
        }
        return campaigns;
    }

    @Override
    public List<MarketingCampaign> getAvailableMarketingCampaignsByRegionId(long regionId) {
        List<MarketingCampaign> campaigns = new ArrayList<>();
        LOG.info("Retrieving available marketing campaigns for region with id = " + regionId);
        List<TariffRegion> tariffs = tariffRegionDao.getAllByRegionId(regionId);
        if (tariffs != null) {
            for (TariffRegion tariff : tariffs) {
                campaigns.addAll(marketingCampaignDao.getAllByTariffRegion(tariff.getId()));
            }
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
        Long tariffId = campaign.getTariffRegion().getTariff().getId();
        tariffService.activateTariffForSingleCustomer(tariffId, customer);
    }

    @Override
    public Map<String, Object> getMarketingCampaignsTable(int page, int size, String name, int status) {
        Query query = this.buildQueryForMarketingCampaignTable(page, size, name, status);
        Map<String, Object> response = new HashMap<>();
        response.put("campaigns", this.marketingCampaignDao
                .executeForList(query.getQuery(), query.getPreparedStatementParams().toArray()));
        response.put("campaignsFound", this.marketingCampaignDao.executeForInt(query.getCountQuery(),
                query.getCountParams().toArray()));
        return response;
    }

    private Query buildQueryForMarketingCampaignTable(int page, int size, String name, int status) {
        Query.Builder builder = new Query.Builder("marketing_campaign");
        builder.where().addLikeCondition("name", name);
        if (status == 1) {
            builder.and().addCondition("marketing_campaign_status = ? ", "ACTIVATED");
        } else if (status == 2) {
            builder.and().addCondition("marketing_campaign_status = ? ", "DEACTIVATED");
        }
        builder.addPaging(page, size);
        return builder.build();
    }

    @Override
    public void updateMarketingCampaignStatus(Long campaignId, ProductStatus productStatus) {
        marketingCampaignDao.updateMarketingCampaignStatus(campaignId, productStatus);
    }

    @Override
    public MarketingCampaign save(MarketingCampaign entity) {
        MarketingCampaign campaign = super.save(entity);
        for(MarketingCampaignServices service: entity.getServices()) {
            marketingCampaignServicesDao.save(service, campaign);
        }
        return campaign;
    }

    @Override
    public MarketingCampaign update(MarketingCampaign entity) {
        MarketingCampaign campaign = super.update(entity);
        for(MarketingCampaignServices service: entity.getServices()) {
            if(service.getId() != null) {
                marketingCampaignServicesDao.update(service);
            } else {
                marketingCampaignServicesDao.save(service, campaign);
            }
        }
        return campaign;
    }
}
