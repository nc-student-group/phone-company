package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CustomerTariffServiceImpl extends CrudServiceImpl<CustomerTariff>
        implements CustomerTariffService {

    private CustomerTariffDao customerTariffDao;
    private OrderService orderService;
    private MailMessageCreator<Tariff> tariffSuspensionNotificationEmailCreator;
    private MailMessageCreator<Tariff> tariffResumingNotificationEmailCreator;
    private EmailService<User> emailService;

    @Autowired
    public CustomerTariffServiceImpl(CustomerTariffDao customerTariffDao, OrderService orderService,
                                     @Qualifier("tariffSuspensionNotificationEmailCreator")
                                             MailMessageCreator<Tariff> tariffSuspensionNotificationEmailCreator,
                                     @Qualifier("tariffResumingNotificationEmailCreator")
                                             MailMessageCreator<Tariff> tariffResumingNotificationEmailCreator,
                                     EmailService<User> emailService) {
        super(customerTariffDao);
        this.customerTariffDao = customerTariffDao;
        this.orderService = orderService;
        this.tariffSuspensionNotificationEmailCreator = tariffSuspensionNotificationEmailCreator;
        this.tariffResumingNotificationEmailCreator = tariffResumingNotificationEmailCreator;
        this.emailService = emailService;
    }

    @Override
    public List<CustomerTariff> getByClientId(Customer customer) {
        return customer.getRepresentative() ?
                customerTariffDao.getCustomerTariffsByCorporateId(customer.getCorporate().getId()) :
                customerTariffDao.getCustomerTariffsByCustomerId(customer.getId());
    }

    @Override
    public CustomerTariff getCurrentCustomerTariff(Customer customer) {
        if (customer.getCorporate() == null) {
            return this.getCurrentCustomerTariff(customer.getId());
        } else {
            if (customer.getCorporate() != null && customer.getRepresentative()) {
                return this.getCurrentCorporateTariff(customer.getCorporate().getId());
            } else {
                throw new ConflictException("You aren't representative of your company. Contact with your company representative.");
            }
        }
    }

    @Override
    public CustomerTariff getCurrentCustomerTariff(long customerId) {
        return this.customerTariffDao.getCurrentCustomerTariff(customerId);
    }

    @Override
    public CustomerTariff getCurrentCorporateTariff(long corporateId) {
        return this.customerTariffDao.getCurrentCorporateTariff(corporateId);
    }

    @Override
    public CustomerTariff getCurrentActiveOrSuspendedClientTariff(Customer customer) {
        return customer.getRepresentative() ?
                customerTariffDao.getCurrentActiveOrSuspendedCorporateTariff(customer.getCorporate().getId()) :
                customerTariffDao.getCurrentActiveOrSuspendedCustomerTariff(customer.getId());
    }

    @Override
    public CustomerTariff getCurrentActiveOrSuspendedCorporateTariff(long corporateId) {
        return customerTariffDao.getCurrentActiveOrSuspendedCorporateTariff(corporateId);
    }

    @Override
    public CustomerTariff deactivateCustomerTariff(CustomerTariff customerTariff) {
        if (CustomerProductStatus.SUSPENDED.equals(customerTariff.getCustomerProductStatus())) {
            Order resumingOrder = orderService.getResumingOrderByCustomerTariff(customerTariff);
            resumingOrder.setOrderStatus(OrderStatus.CANCELED);
            orderService.update(resumingOrder);
        }
        customerTariff.setCustomerProductStatus(CustomerProductStatus.DEACTIVATED);
        LocalDate now = LocalDate.now();

        Order deactivationOrder = new Order();
        deactivationOrder.setCustomerTariff(customerTariff);
        deactivationOrder.setCreationDate(now);
        deactivationOrder.setExecutionDate(now);
        deactivationOrder.setOrderStatus(OrderStatus.DONE);
        deactivationOrder.setType(OrderType.DEACTIVATION);

        customerTariffDao.update(customerTariff);
        orderService.save(deactivationOrder);

        return customerTariff;
    }

    @Override
    public CustomerTariff resumeCustomerTariff(CustomerTariff customerTariff) {
        LocalDate now = LocalDate.now();

        Order pendingResumingOrder = orderService.
                getResumingOrderByCustomerTariff(customerTariff);
        pendingResumingOrder.setOrderStatus(OrderStatus.CANCELED);

        Order resumingOrder = new Order(null, customerTariff,
                OrderType.RESUMING, OrderStatus.DONE, now, now);

        customerTariff.setCustomerProductStatus(CustomerProductStatus.ACTIVE);

        customerTariffDao.update(customerTariff);
        orderService.update(pendingResumingOrder);
        orderService.save(resumingOrder);

        SimpleMailMessage notificationMessage = this.tariffResumingNotificationEmailCreator
                .constructMessage(customerTariff.getTariff());
        this.emailService.sendMail(notificationMessage, customerTariff.getCustomer());

        return customerTariff;
    }

    @Override
    public CustomerTariff suspendCustomerTariff(Map<String, Object> suspensionData) {

        CustomerTariff customerTariff = customerTariffDao.
                getById((new Long((Integer) suspensionData.get("currentTariffId"))));
        Integer daysToExecution = (Integer) suspensionData.get("daysToExecution");

        customerTariff.setCustomerProductStatus(CustomerProductStatus.SUSPENDED);

        LocalDate now = LocalDate.now();
        LocalDate executionDate = now.plusDays(daysToExecution);

        Order suspensionOrder = new Order(null, customerTariff,
                OrderType.SUSPENSION, OrderStatus.DONE, now, now);

        Order resumingOrder = new Order(null, customerTariff,
                OrderType.RESUMING, OrderStatus.PENDING, now, executionDate);

        customerTariffDao.update(customerTariff);
        orderService.save(suspensionOrder);
        orderService.save(resumingOrder);

        return customerTariff;
    }
}
