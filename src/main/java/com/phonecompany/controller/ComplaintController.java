package com.phonecompany.controller;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "api/complaints")
public class ComplaintController {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintController.class);

    private ComplaintService complaintService;
    private UserService userService;

    @Autowired
    public ComplaintController(ComplaintService complaintService,
                               UserService userService) {
        this.complaintService = complaintService;
        this.userService = userService;
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createComplaint(@RequestBody Complaint complaint) {
        if(complaint.getUser().getEmail() == null) {
            User loggedInUser = this.userService.getCurrentlyLoggedInUser();
            complaint.setUser(loggedInUser);
            Complaint createdComplaint = complaintService.createComplaint(complaint);
            LOG.debug("Complaint added {}", createdComplaint);
        }
        else {
            User persistedUser = this.userService.findByEmail(complaint.getUser().getEmail());
            if (persistedUser != null) {
                complaint.setUser(persistedUser);
                Complaint createdComplaint = complaintService.createComplaint(complaint);
                LOG.debug("Complaint added {}", createdComplaint);
            } else {
                LOG.info("User with email " + complaint.getUser().getEmail() + " not found!");
                //complaint = null;
            }
        }
        return new ResponseEntity<>(complaint, HttpStatus.OK);
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
}
