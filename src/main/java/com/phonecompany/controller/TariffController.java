package com.phonecompany.controller;


import com.phonecompany.model.*;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.CustomerServiceImpl;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(value = "/api/tariffs")
public class TariffController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffController.class);

    private RegionService regionService;
    private TariffRegionService tariffRegionService;
    private TariffService tariffService;
    private CustomerTariffService customerTariffService;
    private FileService fileService;
    private CustomerService customerService;
    private OrderService orderService;

    @Autowired
    public TariffController(RegionService regionService, TariffRegionService tariffRegionService,
                            TariffService tariffService, CustomerTariffService customerTariffService,
                            FileService fileService, CustomerService customerService,
                            OrderService orderService) {
        this.regionService = regionService;
        this.tariffRegionService = tariffRegionService;
        this.tariffService = tariffService;
        this.customerTariffService = customerTariffService;
        this.fileService = fileService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/{regionId}/{page}/{size}")
    public Map<String, Object> getTariffsByRegionId(@PathVariable("regionId") Long regionId,
                                                    @PathVariable("page") int page,
                                                    @PathVariable("size") int size) {
        LOGGER.debug("Get all tariffs by region id = " + regionId);
        return tariffRegionService.getTariffsTable(regionId, page, size);
    }

    @RequestMapping(value = "/get/available/", method = RequestMethod.GET)
    public List<Tariff> getClientTariffs() {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByEmail(securityUser.getUsername());
        Long regionId = customer.getAddress().getRegion().getId();
        Boolean isRepresentative = customer.getRepresentative();
        LOGGER.debug("Get all tariffs for customer with id = " + customer.getId());
        return tariffService.getByRegionIdAndClient(regionId, isRepresentative);
    }

    @GetMapping(value = "/empty")
    public Tariff getEmptyTariff() {
        return new Tariff();
    }

    @PostMapping(value = "/regions")
    public ResponseEntity<?> saveTariff(@RequestBody List<TariffRegion> tariffRegions) {
        if (tariffRegions.size() > 0) {
            return tariffService.addNewTariff(tariffRegions.get(0).getTariff(), tariffRegions);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addSingleTariff(@RequestBody Tariff tariff) {
        tariffService.addNewTariff(tariff, null);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "api/tariff/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateTariff(@RequestBody List<TariffRegion> tariffRegions) {
        return tariffService.updateTariffAndRegions(tariffRegions);
    }

    @RequestMapping(value = "/api/tariff/update/single", method = RequestMethod.POST)
    public ResponseEntity<?> updateTariffSingle(@RequestBody Tariff tariff) {
        Tariff temp = tariffService.findByTariffName(tariff.getTariffName());
        if (temp != null && !temp.getId().equals(tariff.getId())) {
            return new ResponseEntity<>(new Error("Tariff with name \"" + tariff.getTariffName() + "\" already exist!"), HttpStatus.BAD_REQUEST);
        }
        LOGGER.debug("Is tariff corporate: " + tariff.getIsCorporate());
        tariff.setPictureUrl(fileService.stringToFile(tariff.getPictureUrl(), "tariff/" + tariff.hashCode()));
        Tariff updatedTariff = tariffService.update(tariff);
        tariffRegionService.deleteByTariffId(updatedTariff.getId());
        LOGGER.debug("Tariff added {}", updatedTariff);
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
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByEmail(securityUser.getUsername());
        LOGGER.debug("Trying to retrieve customer tariffs where customer_id = " + customer.getId());
        return this.customerTariffService.getByClientId(customer);
    }

    @RequestMapping(value = "/api/tariff/update/status/{id}/{status}", method = RequestMethod.GET)
    public ResponseEntity<Void> updateTariffStatus(@PathVariable("id") long tariffId,
                                                   @PathVariable("status") ProductStatus productStatus) {
        this.tariffService.updateTariffStatus(tariffId, productStatus);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/tariffs/available/get/{page}/{size}", method = RequestMethod.GET)
    public Map<String, Object> getTariffsAvailableForCustomer(@PathVariable("page") int page,
                                                              @PathVariable("size") int size) {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByEmail(securityUser.getUsername());
        Map<String, Object> response = new HashMap<>();
        if (customer.getCorporate() == null) {
            LOGGER.debug("Get available tariffs for single customer.");
            response.put("tariffs", tariffService.getTariffsAvailableForCustomer(customer.getAddress().getRegion().getId(), page, size));
            response.put("tariffsCount", tariffService.getCountTariffsAvailableForCustomer(customer.getAddress().getRegion().getId()));
        } else {
            if (customer.getRepresentative()) {
                response.put("tariffs", tariffService.getTariffsAvailableForCorporate(page, size));
                response.put("tariffsCount", tariffService.getCountTariffsAvailableForCorporate());
            }
        }
        return response;
    }

    @RequestMapping(value = "/api/tariff/for/customer/get/{id}", method = RequestMethod.GET)
    public Tariff getTariffForCustomerById(@PathVariable("id") long id) {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByEmail(securityUser.getUsername());
        if (customer.getCorporate() == null) {
            return tariffService.getByIdForSingleCustomer(id, customer.getAddress().getRegion().getId());
        } else {
            return tariffService.getById(id);
        }
    }

    @RequestMapping(value = "/api/tariff/by/customer/get", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrentCustomerTariff() {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByEmail(securityUser.getUsername());
        CustomerTariff customerTariff = null;
        if (customer.getCorporate() == null) {
            customerTariff = customerTariffService.getCurrentCustomerTariff(customer.getId());
        } else {
            if (customer.getCorporate() != null && customer.getRepresentative()) {
                customerTariff = customerTariffService.getCurrentCorporateTariff(customer.getCorporate().getId());
                LOGGER.debug("Corporate tariff {}", customerTariff);
            } else {
                return new ResponseEntity<Object>(new Error("You aren't representative of your company. Contact with your company representative."), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<Object>(customerTariff, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/tariff/activate/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> activateTariff(@PathVariable("id") long id) {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return tariffService.activateTariff(id, customerService.findByEmail(securityUser.getUsername()));
    }

    @RequestMapping(value = "/api/customer/tariff", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrentActiveOrSuspendedCustomerTariff() {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByEmail(securityUser.getUsername());
        CustomerTariff customerTariff = customerTariffService.getCurrentActiveOrSuspendedClientTariff(customer);
        return new ResponseEntity<Object>(customerTariff, HttpStatus.OK);
    }

    @PatchMapping(value = "/api/customer/tariff/deactivate")
    public ResponseEntity<Void> deactivateCustomerTariff(@RequestBody CustomerTariff customerTariff) {
        customerTariffService.deactivateCustomerTariff(customerTariff);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/api/customer/tariff/suspend")
    public ResponseEntity<Void> suspendCustomerTariff(@RequestBody Map<String, Object> data) {
        customerTariffService.suspendCustomerTariff(data);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "api/customer/tariffs/history/{page}/{size}", method = RequestMethod.GET)
    public Map<String, Object> getOrdersHistoryPaged(@PathVariable("page") int page,
                                                     @PathVariable("size") int size) {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByEmail(securityUser.getUsername());
        LOGGER.debug("Get all tariff orders by customer id = " + customer);
        Map<String, Object> map = new HashMap<>();
        map.put("ordersFound", orderService.getOrdersCountByClient(customer));
        map.put("orders", orderService.getOrdersHistoryByClient(customer, page, size));
        return map;
    }

}
