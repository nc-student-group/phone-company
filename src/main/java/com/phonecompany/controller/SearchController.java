package com.phonecompany.controller;

import com.mchange.v2.util.DoubleWeakHashMap;
import com.phonecompany.model.*;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/search")
public class SearchController {


    private static final Logger LOGGER = LoggerFactory.getLogger(com.phonecompany.controller.SearchController.class);
    private UserService userService;
    private CustomerService customerService;
    private ComplaintService complaintService;
    private TariffService tariffService;
    private ServiceService serviceService;

    @Autowired
    public SearchController(UserService userService, CustomerService customerService, ComplaintService complaintService, TariffService tariffService,
                            ServiceService serviceService) {
        this.userService = userService;
        this.customerService = customerService;
        this.complaintService = complaintService;
        this.tariffService = tariffService;
        this.serviceService = serviceService;
    }


    @GetMapping(value = "/users/{page}/{size}/{role}/{status}")
    public Map<String, Object> getUsers(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("role") int userRole,
            @PathVariable("status") String status,
            @RequestParam("s") String email) {
        List<User> users = this.userService.getAllUsersSearch(page,size,email, userRole, status);
        Map<String, Object> response = new HashMap<>();

        response.put("users", users);
        response.put("entitiesSelected", this.userService.getCountSearch(page,size,email, userRole, status));

        return response;
    }

    @GetMapping(value = "/customers/{page}/{size}/{status}/{region}")
    public Map<String, Object> getCustomers(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("status") String status,
            @PathVariable("region") int region,
            @RequestParam("e") String email,
            @RequestParam("ph") String phone,
            @RequestParam("c") int corporate,
            @RequestParam("s") String surname) {
        List<Customer> customers = this.customerService.getAllCustomersSearch(page,size,email, phone, surname, corporate, region, status);
        Map<String, Object> response = new HashMap<>();

        response.put("customers", customers);
        response.put("entitiesSelected", this.customerService.getCountSearch(page,size,email, phone, surname, corporate, region, status));

        return response;
    }

    @GetMapping(value = "/complaints/{page}/{size}/{status}/{category}")
    public Map<String, Object> getComplaints(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("status") String status,
            @PathVariable("category") String category,
            @RequestParam("e") String email) {

        return this.complaintService.getAllComplaintsSearch(page,size,email, status, category);
    }


    @GetMapping(value = "/tariffs/{page}/{size}/{status}/{category}")
    public Map<String, Object> getTariffs(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("status") String status,
            @PathVariable("category") String category,
            @RequestParam("n") String name) {
        List<Tariff> tariffs = this.tariffService.getAllTariffsSearch(page,size,name, status, category);
        Map<String, Object> response = new HashMap<>();

        response.put("tariffs", tariffs);
        response.put("entitiesSelected", this.tariffService.getCountSearch(page,size,name, status, category));

        return response;
    }

    @GetMapping(value = "/services/{page}/{size}/{lowerPrice}/{upperPrice}/{status}")
    public Map<String, Object> getServices(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("status") String status,
            @PathVariable("lowerPrice") int lowerPrice,
            @PathVariable("upperPrice") int upperPrice,
            @RequestParam("n") String name) {
        List<Service> services = this.serviceService.getAllServicesSearch(page,size,name, status, lowerPrice,upperPrice);

        Map<String, Object> response = new HashMap<>();

        response.put("services", services);
        response.put("entitiesSelected", this.serviceService.getCountSearch(page,size,name, status, lowerPrice,upperPrice));

        return response;
    }
}
