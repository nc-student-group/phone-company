package com.phonecompany.service.email;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.stereotype.Component;

@Component("complaintAcceptedEmailCreator")
public class ComplaintAcceptedEmailCreator extends AbstractEmailCreator
        implements MailMessageCreator {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailBody(User recipient) {
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