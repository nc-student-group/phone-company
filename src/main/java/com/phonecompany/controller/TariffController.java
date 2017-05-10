package com.phonecompany.controller;


import com.phonecompany.model.Customer;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tariffs")
public class TariffController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffController.class);

    private TariffRegionService tariffRegionService;
    private TariffService tariffService;
    private CustomerService customerService;
    private EmailService<Customer> emailService;
    private MailMessageCreator<Tariff> tariffNotificationMailCreator;
    private MailMessageCreator<Tariff> tariffActivationNotificationMailCreator;

    @Autowired
    public TariffController(TariffRegionService tariffRegionService,
                            TariffService tariffService,
                            CustomerService customerService,
                            EmailService<Customer> emailService,
                            @Qualifier("tariffNotificationEmailCreator")
                            MailMessageCreator<Tariff> tariffNotificationMailCreator,
                            @Qualifier("tariffDeactivationNotificationEmailCreator")
                            MailMessageCreator<Tariff> tariffActivationNotificationMailCreator){
        this.tariffRegionService = tariffRegionService;
        this.tariffService = tariffService;
        this.customerService = customerService;
        this.emailService = emailService;
        this.tariffNotificationMailCreator = tariffNotificationMailCreator;
        this.tariffActivationNotificationMailCreator = tariffActivationNotificationMailCreator;
    }

    @GetMapping(value = "/{regionId}/{page}/{size}")
    public Map<String, Object> getTariffsByRegionId(@PathVariable("regionId") Long regionId,
                                                    @PathVariable("page") int page,
                                                    @PathVariable("size") int size) {
        LOGGER.debug("Get all tariffs by region id = " + regionId);
        return tariffService.getTariffsTable(regionId, page, size);
    }

    @GetMapping(value = "/empty")
    public Tariff getEmptyTariff() {
        return new Tariff();
    }

    @PostMapping
    public ResponseEntity<?> addSingleTariff(@RequestBody Tariff tariff) {
        Tariff savedTariff = tariffService.addNewTariff(tariff);
        SimpleMailMessage notificationMessage = this.tariffNotificationMailCreator
                .constructMessage(savedTariff);
        this.notifyAgreedCustomers(notificationMessage);
        return new ResponseEntity<Object>(savedTariff, HttpStatus.CREATED);
    }

    private void notifyAgreedCustomers(SimpleMailMessage mailMessage) {
        List<Customer> agreedCustomers = this.getAgreedCustomers();
        LOGGER.debug("Customers agreed for mailing: {}", agreedCustomers);
        this.emailService.sendMail(mailMessage, agreedCustomers);
    }

    private List<Customer> getAgreedCustomers() {
        return this.customerService.getAll().stream()
                .filter(Customer::getIsMailingEnabled)
                .collect(Collectors.toList());
    }

    @PutMapping
    public ResponseEntity<?> updateTariffSingle(@RequestBody Tariff tariff) {
        Tariff updatedTariff = tariffService.updateTariff(tariff);
        return new ResponseEntity<Object>(updatedTariff, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public Map<String, Object> getTariffToEditById(@PathVariable("id") long tariffId) {
        Map<String, Object> response = new HashMap<>();
        response.put("tariff", tariffService.getById(tariffId));
        response.put("regions", tariffRegionService.getAllByTariffId(tariffId));
        return response;
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateTariffStatus(@PathVariable("id") long tariffId,
                                                   @RequestBody String productStatus) {
        this.tariffService.updateTariffStatus(tariffId, ProductStatus.valueOf(productStatus));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/available/{page}/{size}")
    public ResponseEntity<?> getTariffsAvailableForCustomer(@PathVariable("page") int page,
                                                            @PathVariable("size") int size) {
        return new ResponseEntity<Object>(tariffService
                .getTariffsAvailableForCustomer(customerService.getCurrentlyLoggedInUser(), page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/available/{customerId}")
    public ResponseEntity<?> getTariffsAvailableForCustomerById(@PathVariable("customerId") long customerId) {
        return new ResponseEntity<Object>(tariffService
                .getTariffsAvailableForCustomer(customerService.getById(customerId)), HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<?> getTariffForCustomerById(@PathVariable("id") long tariffId) {
        return new ResponseEntity<Object>(tariffService.getTariffForCustomer(tariffId,
                customerService.getCurrentlyLoggedInUser()), HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{id}")
    public ResponseEntity<?> activateTariff(@PathVariable("id") long tariffId) {
        Customer currentlyLoggedInUser = customerService.getCurrentlyLoggedInUser();
        Tariff tariff = this.tariffService.getById(tariffId);
        tariffService.activateTariff(tariffId, currentlyLoggedInUser);
        SimpleMailMessage notificationMessage = this.tariffActivationNotificationMailCreator
                .constructMessage(tariff);
        this.emailService.sendMail(notificationMessage, currentlyLoggedInUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{tariffId}/{customerId}")
    public ResponseEntity<?> activateTariff(@PathVariable("tariffId") long tariffId,
                                            @PathVariable("customerId") long customerId) {
        Customer customer = customerService.getById(customerId);
        tariffService.activateTariff(tariffId, customer);
        Tariff tariff = this.tariffService.getById(tariffId);
        SimpleMailMessage notificationMessage = this.tariffActivationNotificationMailCreator
                .constructMessage(tariff);
        this.emailService.sendMail(notificationMessage, customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
