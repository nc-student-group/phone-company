package com.phonecompany.service.email.customer_related_emails;

import com.phonecompany.model.Complaint;
import com.phonecompany.service.email.AbstractEmailCreator;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.stereotype.Component;

@Component
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