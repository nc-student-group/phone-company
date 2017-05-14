package com.phonecompany.controller;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.Customer;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by nik9str on 14.05.2017.
 */
@RestController
@RequestMapping(value = "/api/search")
public class SearchController {


        private static final Logger LOGGER = LoggerFactory.getLogger(com.phonecompany.controller.SearchController.class);
        private UserService userService;
        private CustomerService customerService;
        private ComplaintService complaintService;
        @Autowired
        public SearchController(UserService userService, CustomerService customerService,ComplaintService complaintService) {
            this.userService = userService;
            this.customerService= customerService;
            this.complaintService =complaintService;
        }




        @GetMapping(value = "/users/{role}/{status}")
        public List<User> getUsers(
                                   @PathVariable("role") int userRole,
                                   @PathVariable("status") String status,
                                   @RequestParam("s") String email) {
            List<User> users = this.userService.getAllUsersSearch(email, userRole, status);
            return users;
        }

    @GetMapping(value = "/customers/{status}/{region}")
    public List<Customer> getCustomers(
            @PathVariable("status") String status,
            @PathVariable("region") int region,
            @RequestParam("e") String email,
            @RequestParam("ph") String phone,
            @RequestParam("c") int corporate,
            @RequestParam("s") String surname) {
        List<Customer> customers = this.customerService.getAllCustomersSearch(email,phone,surname,corporate,region,status);
        return customers;
    }

    @GetMapping(value = "/complaints/{status}/{category}")
    public List<Complaint> getComplaints(
            @PathVariable("status") String status,
            @PathVariable("category") String category,
            @RequestParam("e") String email) {
        List<Complaint> complaints = this.complaintService.getAllComplaintsSearch(email,status,category);
        return complaints;
    }
}
