package com.phonecompany.service.email;

import com.phonecompany.model.Service;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.stereotype.Component;

@Component("serviceNotificationEmailCreator")
public class ServiceNotificationEmailCreator extends AbstractEmailCreator<Service>
        implements MailMessageCreator<Service> {

    @Override
    public String getEmailBody(Service service) {
        return null;
    }

    @Override
    public String getEmailSubject() {
        return "New Services notification";
    }
}
