package com.phonecompany.service.email.service_related_emails;

import com.phonecompany.model.Service;
import com.phonecompany.service.email.AbstractEmailCreator;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component("serviceNotificationEmailCreator")
public class ServiceNotificationEmailCreator extends AbstractEmailCreator<Service>
        implements MailMessageCreator<Service> {

    private TemplateEngine templateEngine;

    @Autowired
    public ServiceNotificationEmailCreator (TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getEmailBody(Service service) {
        Context context = new Context();
        context.setVariable("serviceName", service.getServiceName());
        context.setVariable("price", service.getPrice());
        return this.templateEngine
                .process("new-services-notification", context);
    }

    @Override
    public String getEmailSubject() {
        return "New services notification";
    }
}
