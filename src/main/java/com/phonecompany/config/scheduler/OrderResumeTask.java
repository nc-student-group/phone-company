package com.phonecompany.config.scheduler;

import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.service.email.tariff_related_emails.TariffResumingNotificationEmailCreator;
import com.phonecompany.service.interfaces.CustomerServiceService;
import com.phonecompany.service.interfaces.CustomerTariffService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class OrderResumeTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResumeTask.class);

    private OrderService orderService;
    private CustomerTariffService customerTariffService;
    private CustomerServiceService customerServiceService;
    private TariffResumingNotificationEmailCreator tariffResumingNotificationEmailCreator;
    private EmailService<Customer> emailService;

    @Autowired
    public OrderResumeTask(OrderService orderService,
                           CustomerTariffService customerTariffService,
                           CustomerServiceService customerServiceService,
                           TariffResumingNotificationEmailCreator tariffResumingNotificationEmailCreator,
                           EmailService<Customer> emailService) {
        this.orderService = orderService;
        this.customerTariffService = customerTariffService;
        this.customerServiceService = customerServiceService;
        this.tariffResumingNotificationEmailCreator = tariffResumingNotificationEmailCreator;
        this.emailService = emailService;
    }

    public void resumeNextOrder() {
        Order nextOrder = orderService.getNextResumingOrder();
        if (nextOrder != null) {
            CustomerTariff customerTariff = nextOrder.getCustomerTariff();
            LOGGER.debug("Customer tariff != null: {}", customerTariff != null);
            if (customerTariff != null) {
                LOGGER.debug("Resuming customer tariff {}", customerTariff);
                customerTariffService.resumeCustomerTariff(nextOrder);
                this.sendNotificationMessage(customerTariff);
            }
            if (nextOrder.getCustomerService() != null) {
                LOGGER.debug("Resuming customer service {}", nextOrder.getCustomerService());
                customerServiceService.resumeCustomerService(nextOrder);
            }
        }
    }

    private void sendNotificationMessage(CustomerTariff customerTariff) {
        if (customerTariff.getCustomer() != null) {
            SimpleMailMessage notificationMessage = this.tariffResumingNotificationEmailCreator
                    .constructMessage(customerTariff.getTariff());
            this.emailService.sendMail(notificationMessage, customerTariff.getCustomer());
        }
    }
}
