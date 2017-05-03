package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.interfaces.CustomerTariffService;
import com.phonecompany.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CustomerTariffServiceImpl extends CrudServiceImpl<CustomerTariff> implements CustomerTariffService {

    private CustomerTariffDao customerTariffDao;
    private OrderService orderService;

    @Autowired
    public CustomerTariffServiceImpl(CustomerTariffDao customerTariffDao, OrderService orderService){
        super(customerTariffDao);
        this.customerTariffDao = customerTariffDao;
        this.orderService = orderService;
    }

    @Override
    public List<CustomerTariff> getByClientId(Customer customer) {
        return customer.getRepresentative() ?
                customerTariffDao.getCustomerTariffsByCorporateId(customer.getCorporate().getId()) :
                customerTariffDao.getCustomerTariffsByCustomerId(customer.getId());
    }

    @Override
    public CustomerTariff getCurrentCustomerTariff(long customerId){
        return this.customerTariffDao.getCurrentCustomerTariff(customerId);
    }

    @Override
    public CustomerTariff getCurrentCorporateTariff(long corporateId){
        return this.customerTariffDao.getCurrentCorporateTariff(corporateId);
    }

    @Override
    public CustomerTariff getCurrentActiveOrSuspendedClientTariff(Customer customer) {
        return customer.getRepresentative() ?
                customerTariffDao.getCurrentActiveOrSuspendedCorporateTariff(customer.getCorporate().getId()) :
                customerTariffDao.getCurrentActiveOrSuspendedCustomerTariff(customer.getId());
    }

    @Override
    public CustomerTariff deactivateCustomerTariff(CustomerTariff customerTariff) {
        if(CustomerProductStatus.SUSPENDED.equals(customerTariff.getCustomerProductStatus())) {
            Order resumingOrder = orderService.getResumingOrderByCustomerTariff(customerTariff);
            resumingOrder.setOrderStatus(OrderStatus.CANCELED);
            orderService.update(resumingOrder);
        }
        customerTariff.setCustomerProductStatus(CustomerProductStatus.DEACTIVATED);
        LocalDate now  = LocalDate.now();

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
    public CustomerTariff suspendCustomerTariff(Map<String, Object> suspensionData) {

        CustomerTariff customerTariff = customerTariffDao.
                getById((new Long((Integer)suspensionData.get("currentTariffId"))));
        Integer daysToExecution = (Integer) suspensionData.get("daysToExecution");

        customerTariff.setCustomerProductStatus(CustomerProductStatus.SUSPENDED);

        LocalDate now  = LocalDate.now();
        LocalDate executionDate = now.plusDays(daysToExecution);

        Order suspensionOrder = new Order();
        suspensionOrder.setType(OrderType.SUSPENSION);
        suspensionOrder.setOrderStatus(OrderStatus.DONE);
        suspensionOrder.setCreationDate(now);
        suspensionOrder.setCustomerTariff(customerTariff);
        suspensionOrder.setExecutionDate(now);

        Order resumingOrder = new Order();
        resumingOrder.setType(OrderType.RESUMING);
        resumingOrder.setOrderStatus(OrderStatus.PENDING);
        resumingOrder.setCreationDate(now);
        resumingOrder.setCustomerTariff(customerTariff);
        resumingOrder.setExecutionDate(executionDate);

        customerTariffDao.update(customerTariff);
        orderService.save(suspensionOrder);
        orderService.save(resumingOrder);

        return customerTariff;
    }
}
