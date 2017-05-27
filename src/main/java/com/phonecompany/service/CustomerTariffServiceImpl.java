package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.email.tariff_related_emails.TariffResumingNotificationEmailCreator;
import com.phonecompany.service.email.tariff_related_emails.TariffSuspensionNotificationEmailCreator;
import com.phonecompany.service.interfaces.CustomerTariffService;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ServiceStereotype
public class CustomerTariffServiceImpl extends CrudServiceImpl<CustomerTariff>
        implements CustomerTariffService {

    private CustomerTariffDao customerTariffDao;
    private OrderService orderService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerTariffService.class);

    @Autowired
    public CustomerTariffServiceImpl(CustomerTariffDao customerTariffDao,
                                     OrderService orderService) {
        this.customerTariffDao = customerTariffDao;
        this.orderService = orderService;
    }

    @Override
    public List<CustomerTariff> getByClientId(Customer customer) {
        return customer.getCorporate() != null ?
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
        return customer.getCorporate() != null ?
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

        return customerTariff;
    }

    @Override
    public void resumeCustomerTariff(Order order) {
        LOGGER.debug("RESUMING ORDER ID {}", order.getId());
        order.getCustomerTariff().setCustomerProductStatus(CustomerProductStatus.ACTIVE);
        customerTariffDao.update(order.getCustomerTariff());
        order.setOrderStatus(OrderStatus.DONE);
        orderService.update(order);
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
                OrderType.RESUMING, OrderStatus.CREATED, now, executionDate);

        customerTariffDao.update(customerTariff);
        orderService.save(suspensionOrder);
        orderService.save(resumingOrder);
        this.scheduleCustomerTariffResuming(resumingOrder);
        return customerTariff;
    }

    private void scheduleCustomerTariffResuming(Order resumingOrder) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduledExecutorService);
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                resumeCustomerTariff(resumingOrder);
            }
        }, TypeMapper.toUtilDate(resumingOrder.getExecutionDate()));
    }
}
