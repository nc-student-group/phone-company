package com.phonecompany.controller;

import com.phonecompany.model.Address;
import com.phonecompany.model.Customer;
import com.phonecompany.model.User;
import com.phonecompany.model.events.OnRegistrationCompleteEvent;
import com.phonecompany.model.events.OnUserCreationEvent;
import com.phonecompany.service.interfaces.AddressService;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.UserService;
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
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phonecompany.model.enums.UserRole.CLIENT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    private CustomerService customerService;
    private AddressService addressService;
    private ApplicationEventPublisher eventPublisher;
    private UserService userService;

    @Autowired
    public CustomerController(CustomerService customerService,
                              AddressService addressService,
                              ApplicationEventPublisher eventPublisher,
                              UserService userService) {
        this.customerService = customerService;
        this.addressService = addressService;
        this.eventPublisher = eventPublisher;
        this.userService = userService;
    }

    @RequestMapping(method = POST, value = "/api/customers")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        LOG.debug("Customer retrieved from the http request: " + customer);
        customer.setRole(CLIENT); //TODO: extract logic to service
        Address savedAddress = this.addressService.save(customer.getAddress());
        customer.setAddress(savedAddress);
        Customer persistedCustomer = this.customerService.save(customer);
        LOG.debug("Customer persisted with an id: " + persistedCustomer.getId());

        this.eventPublisher.publishEvent(new OnRegistrationCompleteEvent(persistedCustomer));

        return new ResponseEntity<>(persistedCustomer, HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/customers/new")
    public Customer getEmptyCustomer() {
        return new Customer();
    }

    @RequestMapping(method = GET, value = "/api/customers/{page}/{size}/{rId}/{status}")
    public Map<String, Object> getAllCustomers(@PathVariable("page") int page, @PathVariable("size") int size,
                                               @PathVariable("rId") long rId, @PathVariable("status") String status) {
        LOG.info("Retrieving all the users contained in the database");
        List<Customer> customers = this.customerService.getAllCustomersPaging(page, size, rId, status);
        LOG.info("Users fetched from the database: " + customers);

        Map<String, Object> response = new HashMap<>();
        response.put("customers", customers);
        response.put("customersSelected", customerService.getCountCustomers(rId, status));

        return response;
    }

    @GetMapping("/api/confirmRegistration")
    public ResponseEntity<? extends User> confirmRegistration(@RequestParam String token)
            throws URISyntaxException {
        LOG.debug("Token retrieved from the request parameter: {}", token);
        this.customerService.activateUserByToken(token);
        HttpHeaders redirectionHeaders = this.getRedirectionHeaders();

        return new ResponseEntity<>(redirectionHeaders, HttpStatus.SEE_OTHER);
    }

    private HttpHeaders getRedirectionHeaders() throws URISyntaxException {
        URI registration = new URI("https://phone-company.herokuapp.com/#/login/success");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(registration);
        return httpHeaders;
    }

    @RequestMapping(method = POST, value = "/api/customer/save")
    public ResponseEntity<?> saveCustomerByAdmin(@RequestBody Customer customer) {
        LOG.debug("Customer retrieved from the http request: " + customer);
        if (customerService.findByEmail(customer.getEmail()) == null) {
            customer.setPassword(new BigInteger(50, new SecureRandom()).toString(32));
            eventPublisher.publishEvent(new OnUserCreationEvent(customer));
        }
        Customer persistedCustomer = this.customerService.save(customer);
        return new ResponseEntity<>(persistedCustomer, HttpStatus.CREATED);
    }

    @RequestMapping(method = GET, value = "/api/customer/get")
    public Customer getCustomerByCurrentUserId() {
        User loggedInUser = this.userService.getCurrentlyLoggedInUser();
        return customerService.getById(loggedInUser.getId());
    }
}
