package com.phonecompany.controller;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.service.interfaces.ComplaintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ComplaintController {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintController.class);

    private ComplaintService complaintService;
    @Autowired
    private UserController userController;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @RequestMapping(value = "/api/complaint/add", method = RequestMethod.POST)
    public ResponseEntity<?> createComplaint(@RequestBody Complaint complaint) {
        complaint.setUser(userController.getUser());
        Complaint createdComplaint = complaintService.createComplaint(complaint);
        LOG.debug("Complaint added {}", createdComplaint);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(method = GET, value = "/api/complaints")
    public Collection<Complaint> getAllComplaints() {
        LOG.info("Retrieving all the complaints contained in the database");
        List<Complaint> complaints = complaintService.getAll();
        LOG.info("Complaints fetched from the database: " + complaints);

        return Collections.unmodifiableCollection(complaints);
    }

    @RequestMapping(method = GET, value = "/api/complaintCategory/get")
    public Collection<ComplaintCategory> getAllComplaintCategory() {
        LOG.info("Retrieving all the complaint categories");

        return complaintService.getAllComplaintCategory();
    }
}
