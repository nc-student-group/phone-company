package com.phonecompany.service.email;

import com.phonecompany.model.Tariff;
import com.phonecompany.service.interfaces.MailMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("tariffNotificationEmailCreator")
public class TariffNotificationEmailCreator extends AbstractEmailCreator<Tariff>
        implements MailMessageCreator<Tariff> {

    private static final Logger LOG = LoggerFactory.getLogger(TariffNotificationEmailCreator.class);

    @Override
    public String getEmailBody(Tariff tariff) {
        return "New tariff";
    }

    @Override
    public String getEmailSubject() {
        return "New tariffs notification";
    }
}
