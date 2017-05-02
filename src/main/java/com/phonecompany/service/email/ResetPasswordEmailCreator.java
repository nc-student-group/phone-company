package com.phonecompany.service.email;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.stereotype.Component;

@Component("resetPassMessageCreator")
public class ResetPasswordEmailCreator extends AbstractEmailCreator<User>
        implements MailMessageCreator<User> {

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
