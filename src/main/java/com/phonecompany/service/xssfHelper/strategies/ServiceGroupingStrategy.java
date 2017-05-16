package com.phonecompany.service.xssfHelper.strategies;

import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.Order;
import com.phonecompany.model.Service;
import com.phonecompany.service.xssfHelper.strategies.GroupingStrategy;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@inheritDoc}
 */
public class ServiceGroupingStrategy extends GroupingStrategy<Order, String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Order, String> getValueToKeyMapper() {
        return order -> {
            CustomerServiceDto customerService = order.getCustomerService();
            Service service = customerService.getService();
            return service.getServiceName();
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Predicate<Order> getFilteringCondition(String serviceName) {
        return order -> order.getCustomerService()
                .getService().getServiceName().equals(serviceName);
    }
}
