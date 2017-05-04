package com.phonecompany.service.email;

import com.phonecompany.model.Service;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component("serviceActivationNotificationEmailCreator")
public class ServiceActivationNotificationEmailCreator extends AbstractEmailCreator<Service>
        implements MailMessageCreator<Service> {

    private TemplateEngine templateEngine;

    @Autowired
    public ServiceActivationNotificationEmailCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getEmailBody(Service service) {
        Context context = new Context();
        context.setVariable("tariffName", service.getServiceName());
        return this.templateEngine
                .process("service-activation-notification", context);
    }

    @Override
    public String getEmailSubject() {
        return "Service activation notification";
    }
}
