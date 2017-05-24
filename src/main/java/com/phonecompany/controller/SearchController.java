package com.phonecompany.controller;

import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

        return this.userService.getAllUsersSearch(page,size,email, userRole, status);
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

        return this.customerService.getAllCustomersSearch(page,size,email, phone, surname, corporate, region, status);
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
        return this.tariffService.getAllTariffsSearch(page,size,name, status, category);
    }

    @GetMapping(value = "/services/{page}/{size}/{lowerPrice}/{upperPrice}/{status}")
    public Map<String, Object> getServices(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("status") String status,
            @PathVariable("lowerPrice") int lowerPrice,
            @PathVariable("upperPrice") int upperPrice,
            @RequestParam("n") String name) {

        return this.serviceService.getAllServicesSearch(page,size,name, status, lowerPrice,upperPrice);
    }
}
