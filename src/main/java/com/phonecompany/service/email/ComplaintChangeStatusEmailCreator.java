package com.phonecompany.service.email;

import com.phonecompany.model.Complaint;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.stereotype.Component;

@Component("complaintAcceptedEmailCreator")
public class ComplaintChangeStatusEmailCreator extends AbstractEmailCreator<Complaint>
        implements MailMessageCreator<Complaint> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailBody(Complaint complaint) {
        String msg = "Your complaint is " + complaint.getStatus() + ". \n";
        if (complaint.getComment() != null) msg += complaint.getComment();
        return msg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailSubject() {
        return "State of a complaint";
    }
}