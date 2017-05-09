package com.phonecompany.controller;

import com.phonecompany.model.Customer;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private CustomerService customerService;
    private OrderService orderService;

    @Autowired
    public OrderController(CustomerService customerService,
                           OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/history/{page}/{size}")
    public Map<String, Object> getOrdersHistoryPaged(@PathVariable("page") int page,
                                                     @PathVariable("size") int size) {
        Customer customer = customerService.getCurrentlyLoggedInUser();
        LOGGER.debug("Get all tariff orders by customer id = " + customer);
        Map<String, Object> map = new HashMap<>();
        map.put("ordersFound", orderService.getOrdersCountByClient(customer));
        map.put("orders", orderService.getOrdersHistoryByClient(customer, page, size));
        return map;
    }

    @GetMapping(value = "/history/customer/{id}/{page}/{size}")
    public Map<String, Object> getOrdersHistoryByCustomerIdPaged(@PathVariable("id") long customerId,
                                                                 @PathVariable("page") int page,
                                                                 @PathVariable("size") int size) {
        Customer customer = customerService.getById(customerId);
        LOGGER.debug("Get all tariff orders by customer id = " + customer);
        Map<String, Object> map = new HashMap<>();
        map.put("ordersFound", orderService.getOrdersCountByClient(customer));
        map.put("orders", orderService.getOrdersHistoryByClient(customer, page, size));
        return map;
    }
}
