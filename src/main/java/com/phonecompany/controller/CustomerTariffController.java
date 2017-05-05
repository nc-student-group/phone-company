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

    @Autowired
    public CustomerTariffController(CustomerTariffService customerTariffService,
                                    CustomerService customerService) {
        this.customerTariffService = customerTariffService;
        this.customerService = customerService;
    }

    @GetMapping(value = "/current")
    public ResponseEntity<?> getCurrentCustomerTariff() {
        CustomerTariff customerTariff = customerTariffService.getCurrentCustomerTariff(customerService.getCurrentlyLoggedInUser());
        LOGGER.debug("Current customer tariff {}", customerTariff);
        return new ResponseEntity<Object>(customerTariff, HttpStatus.OK);
    }

    @RequestMapping(value = "/customer-tariff", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrentActiveOrSuspendedCustomerTariff() {
        Customer customer = customerService.getCurrentlyLoggedInUser();
        CustomerTariff customerTariff = customerTariffService.getCurrentActiveOrSuspendedClientTariff(customer);
        return new ResponseEntity<Object>(customerTariff, HttpStatus.OK);
    }
}
