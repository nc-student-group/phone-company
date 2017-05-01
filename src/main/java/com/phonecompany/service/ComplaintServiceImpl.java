package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.service.interfaces.ComplaintService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ComplaintServiceImpl extends CrudServiceImpl<Complaint> implements ComplaintService{

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private ComplaintDao complaintDao;
    private MailMessageCreator<User> complaintAcceptedEmailCreator;
    private EmailService emailService;

    @Autowired
    public ComplaintServiceImpl(ComplaintDao complaintDao,
                                @Qualifier("complaintAcceptedEmailCreator")
                                        MailMessageCreator<User> complaintAcceptedEmailCreator,
                                EmailService emailService){
        super(complaintDao);
        this.complaintDao = complaintDao;
        this.complaintAcceptedEmailCreator = complaintAcceptedEmailCreator;
        this.emailService = emailService;
    }

    @Override
    public Complaint createComplaint(Complaint complaint)
    {
        complaint.setStatus(ComplaintStatus.ACCEPTED);
        complaint.setDate(new java.sql.Date(System.currentTimeMillis()));
        Complaint createdComplaint = complaintDao.save(complaint);
        sendComplaintAcceptedMessage(complaint.getUser());
        LOG.debug("Complaint added {}", complaint);
        return createdComplaint;
    }

    @Override
    public List<ComplaintCategory> getAllComplaintCategory()
    {
        return Arrays.asList(ComplaintCategory.values());
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
                this.complaintAcceptedEmailCreator.constructMessage(user);
        LOG.info("Sending email complaint accepted to: {}", user.getEmail());
        emailService.sendMail(complaintAcceptedMessage);
    }

}
