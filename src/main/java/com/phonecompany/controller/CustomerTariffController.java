package com.phonecompany.controller;

import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.CustomerTariffService;
import com.phonecompany.service.interfaces.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
//TODO: added "s" letter to the customer-tariff resource
//previous: @RequestMapping(value = "/api/customer-tariff")
@RequestMapping(value = "/api/customer-tariffs")
public class CustomerTariffController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerTariffController.class);

    private CustomerTariffService customerTariffService;
    private CustomerService customerService;
    private OrderService orderService;

    @Autowired
    public CustomerTariffController(CustomerTariffService customerTariffService, CustomerService customerService,
                                    OrderService orderService){
        this.customerTariffService = customerTariffService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/current")
    public ResponseEntity<?> getCurrentCustomerTariff() {
        CustomerTariff customerTariff = customerTariffService.getCurrentCustomerTariff();
        LOGGER.debug("Current customer tariff {}", customerTariff);
        return new ResponseEntity<Object>(customerTariff, HttpStatus.OK);
    }

    //TODO: extract to CustomerTariff controller?
    //we are actually dealing with customer tariff here
    @RequestMapping(value = "/tariff", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrentActiveOrSuspendedCustomerTariff() {
        Customer customer = customerService.getCurrentlyLoggedInUser();
        CustomerTariff customerTariff = customerTariffService.getCurrentActiveOrSuspendedClientTariff(customer);
        return new ResponseEntity<Object>(customerTariff, HttpStatus.OK);
    }

    //TODO: extract to CustomerTariff controller?
    //we are actually dealing with customer tariff here
    @RequestMapping(value = "/tariffs/history/{page}/{size}", method = RequestMethod.GET)
    public Map<String, Object> getOrdersHistoryPaged(@PathVariable("page") int page,
                                                     @PathVariable("size") int size) {
        Customer customer = customerService.getCurrentlyLoggedInUser();
        LOGGER.debug("Get all tariff orders by customer id = " + customer);
        Map<String, Object> map = new HashMap<>();
        map.put("ordersFound", orderService.getOrdersCountByClient(customer));
        map.put("orders", orderService.getOrdersHistoryByClient(customer, page, size));
        return map;
    }
}
