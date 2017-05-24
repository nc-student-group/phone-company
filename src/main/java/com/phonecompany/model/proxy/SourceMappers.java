package com.phonecompany.model.proxy;

import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.CustomerTariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    public SourceMappers(CustomerTariffDao customerTariffDao,
                         CustomerServiceDao customerServiceDao) {
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
}

