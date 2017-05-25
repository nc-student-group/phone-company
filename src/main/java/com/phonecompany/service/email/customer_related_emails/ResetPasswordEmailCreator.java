package com.phonecompany.service.email.customer_related_emails;

import com.phonecompany.service.email.AbstractEmailCreator;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordEmailCreator extends AbstractEmailCreator<String>
        implements MailMessageCreator<String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailBody(String password) {
        return "Your new password is " + password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailSubject() {
        return "Password reset";
    }
}
