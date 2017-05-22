package com.phonecompany.service.email.customer_related_emails;

import com.phonecompany.model.User;
import com.phonecompany.service.email.AbstractEmailCreator;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.stereotype.Component;

@Component
public class PasswordAssignmentEmailCreator extends AbstractEmailCreator<User>
        implements MailMessageCreator<User> {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getEmailBody(User recipient) {
        return "Your password is " +
                recipient.getPassword();
    }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getEmailSubject() {
        return "Password assignment";
    }
}
