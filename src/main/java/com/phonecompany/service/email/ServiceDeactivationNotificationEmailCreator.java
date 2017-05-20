package com.phonecompany.service.email;

import com.phonecompany.model.Service;
import com.phonecompany.model.Tariff;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component("serviceDeactivationNotificationEmailCreator")
public class ServiceDeactivationNotificationEmailCreator extends AbstractEmailCreator<Service>
        implements MailMessageCreator<Service> {

    private TemplateEngine templateEngine;

    @Autowired
    public ServiceDeactivationNotificationEmailCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getEmailBody(Service service) {
        Context context = new Context();
        context.setVariable("serviceName", service.getServiceName());
        return this.templateEngine
                .process("service-deactivation-notification", context);
    }

    @Override
    public String getEmailSubject() {
        return "Service deactivation";
    }
}
