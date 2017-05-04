package com.phonecompany.controller;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.Customer;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/complaints")
public class ComplaintController {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintController.class);

    private ComplaintService complaintService;
    private CustomerService customerService;

    @Autowired
    public ComplaintController(ComplaintService complaintService, CustomerService customerService) {
        this.complaintService = complaintService;
        this.customerService = customerService;
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createComplaint(@RequestBody Complaint complaint) {
        LOG.debug("Trying to add complaint {}", complaint);
        Complaint createdComplaint = complaintService.createComplaint(complaint);

        return new ResponseEntity<>(createdComplaint, HttpStatus.OK);
    }

    //TODO: resulting path: /api/complaints/complaints (@RequestMapping(value = "api/complaints") at the top of the class)
    @GetMapping(value = "/complaints")
    public Collection<Complaint> getAllComplaints() {
        LOG.info("Retrieving all the complaints contained in the database");
        List<Complaint> complaints = complaintService.getAll();
        LOG.info("Complaints fetched from the database: " + complaints);

        return Collections.unmodifiableCollection(complaints);
    }

    @GetMapping(value = "/categories")
    public Collection<ComplaintCategory> getAllComplaintCategory() {
        LOG.info("Retrieving all the complaint categories");

        return complaintService.getAllComplaintCategory();
    }

    @GetMapping("/{category}/{page}/{size}")
    public Map<String, Object> getComplaintsByCategory(@PathVariable("category") String category,
                                                       @PathVariable("page") int page,
                                                       @PathVariable("size") int size) {
        LOG.debug("Fetching complaints for the category: {}", category);
        return complaintService.getComplaintsByCategory(category, page, size);
    }

    @GetMapping("/complaint/{id}/{page}/{size}")
    public Map<String, Object> getComplaintsByCustomer(@PathVariable("id") int id,
                                                       @PathVariable("page") int page,
                                                       @PathVariable("size") int size) {
        LOG.debug("Fetching complaints for the customer with id: {}", id);
        return complaintService.getComplaintsByCustomer(id, page, size);
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable("id") long id) {
        LOG.debug("Customer with id: {}", id);
        Customer customer = customerService.getById(id);
        LOG.debug("Found: ", customer);
        return customer;
    }
}
