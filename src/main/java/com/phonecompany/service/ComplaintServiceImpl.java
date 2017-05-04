package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MailMessageCreator;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComplaintServiceImpl extends CrudServiceImpl<Complaint> implements ComplaintService{

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private ComplaintDao complaintDao;
    private MailMessageCreator<Complaint> complaintAcceptedEmailCreator;
    private EmailService emailService;
    private UserService userService;

    @Autowired
    public ComplaintServiceImpl(ComplaintDao complaintDao,
                                @Qualifier("complaintAcceptedEmailCreator")
                                        MailMessageCreator<Complaint> complaintAcceptedEmailCreator,
                                EmailService emailService,
                                UserService userService){
        super(complaintDao);
        this.complaintDao = complaintDao;
        this.complaintAcceptedEmailCreator = complaintAcceptedEmailCreator;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public Complaint createComplaint(Complaint complaint)
    {
        complaint.setStatus(ComplaintStatus.ACCEPTED);
        complaint.setDate(new java.sql.Date(System.currentTimeMillis()));

        if(complaint.getUser().getEmail() == null) {
            User loggedInUser = this.userService.getCurrentlyLoggedInUser();
            complaint.setUser(loggedInUser);
            complaintDao.save(complaint);
            sendComplaintAcceptedMessage(complaint.getUser());
            LOG.debug("Complaint added {}", complaint);
        }
        else {
            User persistedUser = this.userService.findByEmail(complaint.getUser().getEmail());
            if (persistedUser != null) {
                complaint.setUser(persistedUser);
                complaintDao.save(complaint);
                sendComplaintAcceptedMessage(complaint.getUser());
                LOG.debug("Complaint added {}", complaint);
            } else {
                LOG.info("User with email " + complaint.getUser().getEmail() + " not found!");
                //complaint = null;
            }
        }

        return complaint;
    }

    @Override
    public List<ComplaintCategory> getAllComplaintCategory()
    {
        return Arrays.asList(ComplaintCategory.values());
    }

    @Override
    public Map<String, Object> getComplaintsByCategory(String category, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        Object[] args = new Object[]{category, new Long(0)};
        List<Complaint> complaints = this.complaintDao.getPaging(page, size, args);

        LOG.debug("Fetched complaints: {}", complaints);
        response.put("complaints", complaints);
        response.put("complaintsCount", this.complaintDao.getEntityCount(args));
        return response;
    }

    @Override
    public Map<String, Object> getComplaintsByCustomer(int id, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        Object[] args = new Object[]{"-", new Long(id)};
        List<Complaint> complaints = this.complaintDao.getPaging(page, size, args);

        LOG.debug("Fetched complaints: {}", complaints);
        response.put("complaints", complaints);
        response.put("complaintsCount", this.complaintDao.getEntityCount(args));
        return response;
    }

//    @Override
//    public void setStatusIntraprocess(Complaint complaint){
//        complaint.setStatus(ComplaintStatus.INTRAPROCESS);
//        complaintDao.update(complaint);
//    }
//
//    @Override
//    public void setStatusAccomplished(Complaint complaint){
//        complaint.setStatus(ComplaintStatus.ACCOMPLISHED);
//        complaintDao.update(complaint);
//    }

    private void sendComplaintAcceptedMessage(User user) {
        SimpleMailMessage complaintAcceptedMessage =
                this.complaintAcceptedEmailCreator.constructMessage(null);
        LOG.info("Sending email complaint accepted to: {}", user.getEmail());
        emailService.sendMail(complaintAcceptedMessage, user);
    }

}
