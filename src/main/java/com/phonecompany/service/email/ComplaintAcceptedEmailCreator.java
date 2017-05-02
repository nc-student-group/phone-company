package com.phonecompany.service.email;

import com.phonecompany.model.Complaint;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.stereotype.Component;

@Component("complaintAcceptedEmailCreator")
public class ComplaintAcceptedEmailCreator extends AbstractEmailCreator<Complaint>
        implements MailMessageCreator<Complaint> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailBody(Complaint complaint) {
        return "Your complaint is accepted.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailSubject() {
        return "State of a complaint";
    }
}