package com.phonecompany.controller;


import com.phonecompany.model.Customer;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/tariffs")
public class TariffController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffController.class);

    private TariffRegionService tariffRegionService;
    private TariffService tariffService;
    private MailMessageCreator<Tariff> tariffNotificationEmailCreator;
    private MailMessageCreator<Tariff> tariffActivationNotificationEmailCreator;
    private MailMessageCreator<Tariff> tariffDeactivationNotificationEmailCreator;
    private EmailService<Customer> emailService;

    @Autowired
    public TariffController(TariffRegionService tariffRegionService,
                            TariffService tariffService,
                            @Qualifier("tariffNotificationEmailCreator")
                            MailMessageCreator<Tariff> tariffNotificationEmailCreator,
                            @Qualifier("tariffActivationNotificationEmailCreator")
                            MailMessageCreator<Tariff> tariffActivationNotificationEmailCreator,
                            @Qualifier("tariffDeactivationNotificationEmailCreator")
                            MailMessageCreator<Tariff> tariffDeactivationNotificationEmailCreator,
                            EmailService<Customer> emailService) {
        this.tariffRegionService = tariffRegionService;
        this.tariffService = tariffService;
        this.tariffNotificationEmailCreator = tariffNotificationEmailCreator;
        this.tariffActivationNotificationEmailCreator = tariffActivationNotificationEmailCreator;
        this.tariffDeactivationNotificationEmailCreator = tariffDeactivationNotificationEmailCreator;
        this.emailService = emailService;
    }

    @GetMapping(value = "/{regionId}/{page}/{size}")
    public Map<String, Object> getTariffsByRegionId(@PathVariable("regionId") Long regionId,
                                                    @PathVariable("page") int page,
                                                    @PathVariable("size") int size) {
        LOGGER.debug("Get all tariffs by region id = " + regionId);
        return tariffRegionService.getTariffsTable(regionId, page, size);
    }

    @GetMapping(value = "/empty")
    public Tariff getEmptyTariff() {
        return new Tariff();
    }

    //TODO: it is also natural to extract this one to tariff-regions resource
    //@RequestMapping(value = "/api/tariff-regions")
    //public class TariffRegionController
    //.......................................
    //@PostMapping
    @PostMapping(value = "/regions")
    public ResponseEntity<?> saveTariff(@RequestBody List<TariffRegion> tariffRegions) {
        Tariff savedTariff = tariffService.addNewTariff(tariffRegions);
        return new ResponseEntity<Object>(savedTariff, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> addSingleTariff(@RequestBody Tariff tariff) {
        Tariff savedTariff = tariffService.addNewTariff(tariff);

        return new ResponseEntity<Object>(savedTariff, HttpStatus.CREATED);
    }

    //TODO: relates to tariff-regions resource as well
    @PutMapping(value = "/regions")
    public ResponseEntity<?> updateTariff(@RequestBody List<TariffRegion> tariffRegions) {
        Tariff updatedTariff = tariffService.updateTariff(tariffRegions);
        return new ResponseEntity<Object>(updatedTariff, HttpStatus.OK);
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
    public ResponseEntity<Void> updateTariffStatus(@PathVariable("id") long tariffId,
                                                   @RequestBody ProductStatus productStatus) {
        this.tariffService.updateTariffStatus(tariffId, productStatus);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = "/available/{page}/{size}")
    public ResponseEntity<?> getTariffsAvailableForCustomer(@PathVariable("page") int page,
                                                            @PathVariable("size") int size) {
        return new ResponseEntity<Object>(tariffService.getTariffsAvailableForCustomer(page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<?> getTariffForCustomerById(@PathVariable("id") long tariffId) {
        return new ResponseEntity<Object>(tariffService.getTariffForCustomer(tariffId), HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{id}")
    public ResponseEntity<?> activateTariff(@PathVariable("id") long tariffId) {
        tariffService.activateTariff(tariffId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
