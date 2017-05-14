package com.phonecompany.service.xssfHelper;

import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.Tariff;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@inheritDoc}
 */
public class TariffGroupingStrategy extends GroupingStrategy<Order, String> {

    /**
     * {@inheritDoc}
     */
    public Function<Order, String> getValueToKeyMapper() {
        return order -> {
            CustomerTariff customerTariff = order.getCustomerTariff();
            Tariff tariff = customerTariff.getTariff();
            return tariff.getTariffName();
        };
    }

    /**
     * {@inheritDoc}
     */
    public Predicate<Order> getFilteringCondition(String tariffName) {
        return order -> order.getCustomerTariff()
                .getTariff().getTariffName().equals(tariffName);
    }
}
