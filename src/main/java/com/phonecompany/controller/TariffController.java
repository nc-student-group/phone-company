package com.phonecompany.controller;


import com.phonecompany.annotations.ValidateParams;
import com.phonecompany.model.Customer;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.email.tariff_related_emails.TariffDeactivationNotificationEmailCreator;
import com.phonecompany.service.email.tariff_related_emails.TariffNotificationEmailCreator;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/tariffs")
public class TariffController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffController.class);

    private TariffRegionService tariffRegionService;
    private TariffService tariffService;
    private EmailService<Customer> emailService;
    private TariffNotificationEmailCreator tariffNotificationMailCreator;
    private TariffDeactivationNotificationEmailCreator tariffActivationNotificationMailCreator;
    private CustomerService customerService;
    private CorporateService corporateService;

    @Autowired
    public TariffController(TariffRegionService tariffRegionService,
                            TariffService tariffService,
                            EmailService<Customer> emailService,
                            TariffNotificationEmailCreator tariffNotificationMailCreator,
                            TariffDeactivationNotificationEmailCreator tariffActivationNotificationMailCreator,
                            CustomerService customerService,
                            CorporateService corporateService) {
        this.tariffRegionService = tariffRegionService;
        this.tariffService = tariffService;
        this.emailService = emailService;
        this.tariffNotificationMailCreator = tariffNotificationMailCreator;
        this.tariffActivationNotificationMailCreator = tariffActivationNotificationMailCreator;
        this.customerService = customerService;
        this.corporateService = corporateService;
    }


    @GetMapping(value = "/{page}/{size}")
    public Map<String, Object> getTariffs(@PathVariable("page") int page,
                                          @PathVariable("size") int size,
                                          @RequestParam("n") String name,
                                          @RequestParam("s") int status,
                                          @RequestParam("t") int type,
                                          @RequestParam("f") String from,
                                          @RequestParam("to") String to,
                                          @RequestParam("ob") int orderBy,
                                          @RequestParam("ot") String orderByType) {
        return tariffService.getTariffsTable(page, size, name, status, type, from, to, orderBy, orderByType);
    }

    @GetMapping(value = "/region/{id}/{page}/{size}")
    public Map<String, Object> getTariffsForRegion(@PathVariable("page") int page,
                                                   @PathVariable("size") int size,
                                                   @PathVariable("id") Long regionId) {
        return tariffService.getTariffsTable(page, size, regionId);
    }

    @GetMapping(value = "/empty")
    public Tariff getEmptyTariff() {
        return new Tariff();
    }

    @ValidateParams
    @PostMapping
    public ResponseEntity<?> addSingleTariff(@RequestBody Tariff tariff) {
        Tariff savedTariff = tariffService.addNewTariff(tariff);
        SimpleMailMessage notificationMessage = this.tariffNotificationMailCreator
                .constructMessage(savedTariff);
        this.customerService.notifyAgreedCustomers(notificationMessage);
        return new ResponseEntity<Object>(savedTariff, HttpStatus.CREATED);
    }

    @ValidateParams
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
        LOGGER.debug("In tariff status update method: {}", productStatus);
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

    @GetMapping(value = "/corporate/available")
    public ResponseEntity<?> getTariffsAvailableForCorporate() {
        return new ResponseEntity<Object>(tariffService
                .getTariffsAvailableForCorporate(), HttpStatus.OK);
    }

    @GetMapping(value = "/corporate/available/{page}/{size}")
    public Map<String, Object> getTariffsAvailableForCorporatePaged(@PathVariable("page") int page,
                                                                    @PathVariable("size") int size) {
        return tariffService.getTariffsAvailableForCorporatePaged(page, size);
    }

    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<?> getTariffForCustomerById(@PathVariable("id") long tariffId) {
        return new ResponseEntity<Object>(tariffService.getTariffForCustomer(tariffId,
                customerService.getCurrentlyLoggedInUser()), HttpStatus.OK);
    }

    @GetMapping(value = "/tariff/{id}")
    public ResponseEntity<?> getTariffForCorporateById(@PathVariable("id") long tariffId) {
        return new ResponseEntity<Object>(tariffService.getTariff(tariffId), HttpStatus.OK);
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

    @GetMapping(value = "/corporate/activate/{tariffId}/{corporateId}")
    public ResponseEntity<?> activateCorporateTariff(@PathVariable("tariffId") long tariffId,
                                                     @PathVariable("corporateId") long corporateId) {
        tariffService.activateTariffForCorporateCustomer(tariffId, corporateService.getById(corporateId));
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
