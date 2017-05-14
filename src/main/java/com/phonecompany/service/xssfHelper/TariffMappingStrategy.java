package com.phonecompany.service.xssfHelper;

import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.Tariff;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@inheritDoc}
 */
public class TariffMappingStrategy extends MappingStrategy<Order, String> {

    /**
     * {@inheritDoc}
     */
    public Function<Order, String> getObjectToItsIdentifierMapper() {
        return order -> {
            CustomerTariff customerTariff = order.getCustomerTariff();
            Tariff tariff = customerTariff.getTariff();
            return tariff.getTariffName();
        };
    }

    /**
     * {@inheritDoc}
     */
    public Predicate<Order> getObjectFilter(String tariffName) {
        return order -> order.getCustomerTariff()
                .getTariff().getTariffName().equals(tariffName);
    }
}
