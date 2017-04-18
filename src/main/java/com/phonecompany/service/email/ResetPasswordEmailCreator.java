package com.phonecompany.service.email;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component("resetPassMessageCreator")
public class ResetPasswordEmailCreator extends AbstractEmailCreator
        implements MailMessageCreator {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailBody(User recipient) {
        return "Your new password is " +
                recipient.getPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailSubject() {
        return "Password reset";
    }
}
