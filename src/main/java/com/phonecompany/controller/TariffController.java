package com.phonecompany.controller;


import com.phonecompany.model.*;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.CustomerTariffService;
import com.phonecompany.service.interfaces.RegionService;
import com.phonecompany.service.interfaces.TariffRegionService;
import com.phonecompany.service.interfaces.TariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@RestController
public class TariffController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffController.class);

    @Autowired
    private RegionService regionService;

    @Autowired
    private TariffRegionService tariffRegionService;

    @Autowired
    private TariffService tariffService;

    @Autowired
    private CustomerTariffService customerTariffService;

    @Autowired
    private CustomerController customerController;


    @RequestMapping(value = "/api/regions/get", method = RequestMethod.GET)
    public List<Region> getAllRegions() {
        LOGGER.debug("Get all regions.");
        return regionService.getAll();
    }

    @RequestMapping(value = "/api/tariffs/get/by/region/{id}/{page}/{size}", method = RequestMethod.GET)
    public Map<String, Object> getTariffsByRegionId(@PathVariable("id") Long regionId,
                                                    @PathVariable("page") int page,
                                                    @PathVariable("size") int size) {
        LOGGER.debug("Get all tariffs by region id = " + regionId);
        return tariffService.getTariffsTable(regionId, page, size);
    }

    @RequestMapping(value = "api/tariffs/get/available/", method = RequestMethod.GET)
    public List<Tariff> getCustomerTariffs() {
        Customer customer = customerController.getCustomerByCurrentUserId();
        LOGGER.debug("Get all tariffs for customer with id = " + customer.getId());
        return tariffService.getByRegion(customer.getAddress().getRegion().getId());
    }

    @RequestMapping(value = "/api/tariff/new/get", method = RequestMethod.GET)
    public Tariff getEmptyTariff() {
        return new Tariff();
    }

    @RequestMapping(value = "/api/tariff/add", method = RequestMethod.POST)
    public ResponseEntity<?> saveTariff(@RequestBody List<TariffRegion> tariffRegions) {
        if (tariffRegions.size() > 0) {
            Tariff tariff = tariffRegions.get(0).getTariff();
            if(tariffService.findByTariffName(tariff.getTariffName()) != null){
                return new ResponseEntity<>(new Error("Tariff with name \""+tariff.getTariffName()+"\" already exist!"), HttpStatus.BAD_REQUEST);
            }
            tariff.setProductStatus(ProductStatus.ACTIVATED);
            tariff.setCreationDate(new Date(Calendar.getInstance().getTimeInMillis()));
            tariffService.setAutoCommit(false);
            tariffService.beginTransaction();
            try {
                Tariff savedTariff = tariffService.save(tariff);
                LOGGER.debug("Tariff added {}", savedTariff);
                tariffRegions.forEach((TariffRegion tariffRegion) -> {
                    if (tariffRegion.getPrice() > 0 && tariffRegion.getRegion() != null) {
                        tariffRegion.setTariff(savedTariff);
                        tariffRegionService.save(tariffRegion);
                        LOGGER.debug("Tariff-region added {}", tariffRegion);
                    }
                });
                tariffService.commit();
            }catch (Exception e){
                tariffService.rollback();
            }finally {
                tariffService.setAutoCommit(true);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/tariff/add/single", method = RequestMethod.POST)
    public ResponseEntity<?> addSingleTariff(@RequestBody Tariff tariff) {
        if(tariffService.findByTariffName(tariff.getTariffName()) != null){
            return new ResponseEntity<>(new Error("Tariff with name \""+tariff.getTariffName()+"\" already exist!"), HttpStatus.BAD_REQUEST);
        }
        tariff.setProductStatus(ProductStatus.ACTIVATED);
        tariff.setCreationDate(new Date(Calendar.getInstance().getTimeInMillis()));
        Tariff savedTariff = tariffService.save(tariff);
        LOGGER.debug("Tariff added {}", savedTariff);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "api/tariff/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateTariff(@RequestBody List<TariffRegion> tariffRegions) {
        return tariffService.updateTariffAndRegions(tariffRegions);
    }

    @RequestMapping(value = "/api/tariff/update/single", method = RequestMethod.POST)
    public ResponseEntity<?> updateTariffSingle(@RequestBody Tariff tariff) {
        Tariff temp = tariffService.findByTariffName(tariff.getTariffName());
        if(temp != null && temp.getId() != tariff.getId()){
            return new ResponseEntity<>(new Error("Tariff with name \""+tariff.getTariffName()+"\" already exist!"), HttpStatus.BAD_REQUEST);
        }
        LOGGER.debug("Is tariff corporate: " + tariff.getIsCorporate());
        tariffService.setAutoCommit(false);
        tariffService.beginTransaction();
        try {
            Tariff updatedTariff = tariffService.update(tariff);
            tariffRegionService.deleteByTariffId(updatedTariff.getId());
            LOGGER.debug("Tariff added {}", updatedTariff);
            tariffService.commit();
        }catch (Exception e){
            tariffService.rollback();
        }finally {
            tariffService.setAutoCommit(true);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/tariff/get/{id}", method = RequestMethod.GET)
    public Map<String, Object> getTariffToEditById(@PathVariable("id") long tariffId) {
        Map<String, Object> response = new HashMap<>();
        response.put("tariff", tariffService.getById(tariffId));
        response.put("regions", tariffRegionService.getAllByTariffId(tariffId));
        return response;
    }

    @RequestMapping(value = "/api/tariffs/get/by/client", method = RequestMethod.GET)
    public List<CustomerTariff> getTariffsByClientId() {
        Customer customer = customerController.getCustomerByCurrentUserId();
        LOGGER.debug("Trying to retrieve customer tariffs where customer_id = " + customer.getId());
        return this.customerTariffService.getByClientId(customer);
    }

}
