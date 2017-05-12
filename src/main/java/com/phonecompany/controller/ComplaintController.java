package com.phonecompany.controller;

import com.phonecompany.model.Complaint;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
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
    private UserService userService;
    private MailMessageCreator<Complaint> complaintChangeStatusCreator;
    private EmailService<User> emailService;

    @Autowired
    public ComplaintController(ComplaintService complaintService,
                               UserService userService,
                               @Qualifier("complaintAcceptedEmailCreator") MailMessageCreator<Complaint> complaintChangeStatusCreator,
                               EmailService<User> emailService) {
        this.complaintService = complaintService;
        this.userService = userService;
        this.complaintChangeStatusCreator = complaintChangeStatusCreator;
        this.emailService = emailService;
    }

    @PostMapping("/customer")
    public ResponseEntity<?> createComplaint(@RequestBody Complaint complaint) {
        LOG.debug("Trying to add complaint {}", complaint);
        complaint.setUser(userService.getCurrentlyLoggedInUser());
        Complaint createdComplaint = complaintService.createComplaint(complaint);
        sendComplaintChangeStatusMessage(createdComplaint);

        return new ResponseEntity<>(createdComplaint, HttpStatus.OK);
    }

    @PostMapping("/csr")
    public ResponseEntity<?> createComplaintByCsr(@RequestBody Complaint complaint) {
        LOG.debug("Trying to add complaint {}", complaint);
        Complaint createdComplaint = complaintService.createComplaintByCsr(complaint);
        if (createdComplaint.getUser() != null)
            sendComplaintChangeStatusMessage(createdComplaint);

        return new ResponseEntity<>(createdComplaint, HttpStatus.OK);
    }

    @GetMapping
    public Collection<Complaint> getAllComplaints() {
        LOG.info("Retrieving all the complaints contained in the database");
        List<Complaint> complaints = complaintService.getAll();
        LOG.info("Complaints fetched from the database: " + complaints);

        return Collections.unmodifiableCollection(complaints);
    }

    @GetMapping("/{category}/{status}/{page}/{size}")
    public Map<String, Object> getComplaints(@PathVariable("category") String category,
                                             @PathVariable("status") String status,
                                             @PathVariable("page") int page,
                                             @PathVariable("size") int size) {
        LOG.debug("Fetching complaints with the category: {}", category);
        return complaintService.getComplaints(category, status, page, size);
    }

    @GetMapping("/{id}/{page}/{size}")
    public Map<String, Object> getComplaintsByCustomer(@PathVariable("id") int id,
                                                       @PathVariable("page") int page,
                                                       @PathVariable("size") int size) {
        LOG.debug("Fetching complaints for the customer with id: {}", id);
        return complaintService.getComplaintsByCustomer(id, page, size);
    }

    @GetMapping("/pmg/{category}/{page}/{size}")
    public Map<String, Object> getComplaintsByResponsible(@PathVariable("category") String category,
                                                          @PathVariable("page") int page,
                                                          @PathVariable("size") int size) {
        LOG.debug("Fetching complaints with category: {} for the responsible", category);
        return complaintService.getComplaintsByResponsible(userService.getCurrentlyLoggedInUser().getId(),
                category, page, size);
    }

    @PutMapping("/pmg")
    public ResponseEntity<?> handleComplaint(@RequestBody long complaintId) {
        LOG.debug("Handled complaint id: {}", complaintId);
        Complaint complaint = complaintService.getById(complaintId);
        complaint.setResponsiblePmg(userService.getCurrentlyLoggedInUser());
        Complaint updatedComplaint = complaintService.setStatusIntraprocess(complaint);
        if (updatedComplaint != null) sendComplaintChangeStatusMessage(updatedComplaint);
        return new ResponseEntity<>(updatedComplaint, HttpStatus.OK);
    }

    @PutMapping("/pmg/{id}")
    public ResponseEntity<?> completeComplaint(@PathVariable("id") long complaintId,
                                               @RequestBody String comment) {
        LOG.debug("Complete complaint id: {}, comment: {}", complaintId, comment);
        Complaint updatedComplaint = complaintService.setStatusAccomplished(complaintService.getById(complaintId), comment);
        if (updatedComplaint != null) sendComplaintChangeStatusMessage(updatedComplaint);
        return new ResponseEntity<>(updatedComplaint, HttpStatus.OK);
    }

    private void sendComplaintChangeStatusMessage(Complaint complaint) {
        SimpleMailMessage complaintAcceptedMessage =
                this.complaintChangeStatusCreator.constructMessage(complaint);
        LOG.info("Sending email complaint accepted to: {}", complaint.getUser().getEmail());
        emailService.sendMail(complaintAcceptedMessage, complaint.getUser());
    }
}
