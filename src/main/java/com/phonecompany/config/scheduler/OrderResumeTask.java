package com.phonecompany.config.scheduler;

import com.phonecompany.model.Order;
import com.phonecompany.service.interfaces.CustomerServiceService;
import com.phonecompany.service.interfaces.CustomerTariffService;
import com.phonecompany.service.interfaces.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OrderResumeTask {

    private OrderService orderService;
    private CustomerTariffService customerTariffService;
    private CustomerServiceService customerServiceService;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResumeTask.class);

    @Autowired
    public OrderResumeTask(OrderService orderService, CustomerTariffService customerTariffService,
                           CustomerServiceService customerServiceService) {
        this.orderService = orderService;
        this.customerTariffService = customerTariffService;
        this.customerServiceService = customerServiceService;
    }

    public void resumeNextOrder() {
        Order nextOrder = orderService.getNextResumingOrder();
        if (nextOrder != null) {
            LOGGER.debug("Customer tariff != null: {}", nextOrder.getCustomerTariff() != null);
            if (nextOrder.getCustomerTariff() != null) {
                LOGGER.debug("Resuming customer tariff {}", nextOrder.getCustomerTariff());
                customerTariffService.resumeCustomerTariff(nextOrder);
            }
            if (nextOrder.getCustomerService() != null) {
                LOGGER.debug("Resuming customer service {}", nextOrder.getCustomerService());
                customerServiceService.resumeCustomerService(nextOrder);
            }
        }
    }

    public void resumeCustomerTariff() {

    }
}
