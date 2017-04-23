package com.phonecompany.controller;

import com.phonecompany.model.Customer;
import com.phonecompany.model.OnRegistrationCompleteEvent;
import com.phonecompany.model.OnUserCreationEvent;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.security.SecureRandom;

import static com.phonecompany.controller.UserController.USERS_RESOURCE_NAME;
import static com.phonecompany.model.enums.UserRole.CLIENT;
import static com.phonecompany.util.RestUtil.getResourceHeaders;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    private CustomerService customerService;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public CustomerController(CustomerService customerService,
                              ApplicationEventPublisher eventPublisher) {
        this.customerService = customerService;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(method = POST, value = "/api/customers")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        LOG.debug("Customer retrieved from the http request: " + customer);

        customer.setRole(CLIENT);
        Customer persistedCustomer = this.customerService.save(customer);
        LOG.debug("Customer persisted with an id: " + persistedCustomer.getId());

        this.eventPublisher.publishEvent(new OnRegistrationCompleteEvent(persistedCustomer));

        HttpHeaders resourceHeaders = getResourceHeaders(USERS_RESOURCE_NAME, persistedCustomer.getId());

        return new ResponseEntity<>(persistedCustomer, resourceHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = GET, value = "/api/customers")
    public Collection<Customer> getAllCustomers() {
        LOG.info("Retrieving all the users contained in the database");

        List<Customer> customers = this.customerService.getAll();

        LOG.info("Users fetched from the database: " + customers);

        return Collections.unmodifiableCollection(customers);
    }
    @GetMapping("/api/confirmRegistration")
    public ResponseEntity<? extends User> confirmRegistration(@RequestParam String token)
            throws URISyntaxException {
        LOG.debug("Token retrieved from the request parameter: {}", token);
        this.customerService.activateUserByToken(token);

        URI registration = new URI("http://localhost:8090/#/login/success");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(registration);

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @RequestMapping(method = POST, value = "/api/customer/save")
    public ResponseEntity<?> saveCustomerByAdmin(@RequestBody Customer customer) {
        LOG.info(customer.toString());
        customer.setPassword(new BigInteger(50, new SecureRandom()).toString(32));
        eventPublisher.publishEvent(new OnUserCreationEvent(customer));
        Customer persistedCustomer = this.customerService.save(customer);

        HttpHeaders resourceHeaders = getResourceHeaders(USERS_RESOURCE_NAME, persistedCustomer.getId());
        return new ResponseEntity<>(persistedCustomer, resourceHeaders, HttpStatus.CREATED);
    }
}
