package com.phonecompany.service;

import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.Tariff;

import java.util.function.Function;
import java.util.function.Predicate;

public class TariffFilteringStrategy extends FilteringStrategy {

    public Function<Order, String> getOrderToProductNameMapper() {
        return order -> {
            CustomerTariff customerTariff = order.getCustomerTariff();
            Tariff tariff = customerTariff.getTariff();
            return tariff.getTariffName();
        };
    }

    public Predicate<Order> getProductNameFilter(String tariffName) {
        return order -> order.getCustomerTariff()
                .getTariff().getTariffName().equals(tariffName);
    }
}
