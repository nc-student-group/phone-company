package com.phonecompany.controller;


import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.TariffRegionService;
import com.phonecompany.service.interfaces.TariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/tariffs")
public class TariffController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffController.class);

    private TariffRegionService tariffRegionService;
    private TariffService tariffService;
    private CustomerService customerService;

    @Autowired
    public TariffController(TariffRegionService tariffRegionService,
                            TariffService tariffService,
                            CustomerService customerService) {
        this.tariffRegionService = tariffRegionService;
        this.tariffService = tariffService;
        this.customerService = customerService;
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

        return new ResponseEntity<Object>(savedTariff, HttpStatus.CREATED);
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
                                                   @RequestBody String productStatus) {
        this.tariffService.updateTariffStatus(tariffId, ProductStatus.valueOf(productStatus));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = "/available/{page}/{size}")
    public ResponseEntity<?> getTariffsAvailableForCustomer(@PathVariable("page") int page,
                                                            @PathVariable("size") int size) {
        return new ResponseEntity<Object>(tariffService
                .getTariffsAvailableForCustomer(customerService.getCurrentlyLoggedInUser(),page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<?> getTariffForCustomerById(@PathVariable("id") long tariffId) {
        return new ResponseEntity<Object>(tariffService.getTariffForCustomer(tariffId,
                customerService.getCurrentlyLoggedInUser()), HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{id}")
    public ResponseEntity<?> activateTariff(@PathVariable("id") long tariffId) {
        tariffService.activateTariff(tariffId, customerService.getCurrentlyLoggedInUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
