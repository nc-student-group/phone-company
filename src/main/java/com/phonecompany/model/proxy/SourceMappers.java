package com.phonecompany.model.proxy;

import com.phonecompany.dao.interfaces.*;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.MarketingCampaignServices;
import com.phonecompany.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * Provides a number of mappers used to map an id corresponding to
 * some entity from the storage to the respective Java object or
 * {@code Collection} of objects
 */
@Component
public class SourceMappers {

    private static CustomerTariffDao customerTariffDao;
    private static CustomerServiceDao customerServiceDao;
    private static MarketingCampaignServicesDao marketingCampaignServicesDao;

    @Autowired
    public SourceMappers(CustomerTariffDao customerTariffDao,
                         CustomerServiceDao customerServiceDao,
                         MarketingCampaignServicesDao marketingCampaignServicesDao) {
        SourceMappers.customerTariffDao = customerTariffDao;
        SourceMappers.customerServiceDao = customerServiceDao;
    }

    public static final SourceMapper<CustomerServiceDto> CUSTOMER_SERVICE_MAPPER =
            new SourceMapper<CustomerServiceDto>() {
                @Override
                public Function<Long, CustomerServiceDto> getMapper() {
                    return (id) -> customerServiceDao.getById(id);
                }
            };

    public static final SourceMapper<CustomerTariff> CUSTOMER_TARIFF_MAPPER =
            new SourceMapper<CustomerTariff>() {
                @Override
                public Function<Long, CustomerTariff> getMapper() {
                    return (id) -> customerTariffDao.getById(id);
                }
            };

    public static final SourceMapper<List<MarketingCampaignServices>> MARKETING_CAMPAIGN_SERVICES_MAPPER =
            new SourceMapper<List<MarketingCampaignServices>>() {
        @Override
        public Function<Long, List<MarketingCampaignServices>> getMapper() {
            return (id) -> marketingCampaignServicesDao.getServicesByMarketingCampaignId(id);
        }
    };
}
