package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.CustomerServiceService;
import com.phonecompany.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceServiceImpl extends CrudServiceImpl<CustomerServiceDto> implements CustomerServiceService{

    private CustomerServiceDao customerServiceDao;
    private OrderService orderService;

    @Autowired
    public CustomerServiceServiceImpl(CustomerServiceDao customerServiceDao, OrderService orderService){
        super(customerServiceDao);
        this.customerServiceDao = customerServiceDao;
        this.orderService = orderService;
    }

    @Override
    public List<CustomerServiceDto> getCurrentCustomerServices(long customerId) {
        return customerServiceDao.getCurrentCustomerServices(customerId);
    }

    @Override
    public List<CustomerServiceDto> getCustomerServicesByCustomerId(long customerId) {
        return customerServiceDao.getCustomerServicesByCustomerId(customerId);
    }

    @Override
    public CustomerServiceDto deactivateCustomerService(CustomerServiceDto customerService) {
        if(CustomerProductStatus.SUSPENDED.equals(customerService.getOrderStatus())) {
            Order resumingOrder = orderService.getResumingOrderByCustomerService(customerService);
            resumingOrder.setOrderStatus(OrderStatus.CANCELED);
            orderService.update(resumingOrder);
        }
        customerService.setOrderStatus(CustomerProductStatus.DEACTIVATED);
        LocalDate now  = LocalDate.now();

        Order deactivationOrder = new Order();
        deactivationOrder.setCustomerService(customerService);
        deactivationOrder.setCreationDate(now);
        deactivationOrder.setExecutionDate(now);
        deactivationOrder.setOrderStatus(OrderStatus.DONE);
        deactivationOrder.setType(OrderType.DEACTIVATION);

        customerServiceDao.update(customerService);
        orderService.save(deactivationOrder);

        return customerService;
    }

    @Override
    public CustomerServiceDto activateCustomerService(CustomerServiceDto customerService) {

        Order resumingOrder = orderService.getResumingOrderByCustomerService(customerService);
        resumingOrder.setOrderStatus(OrderStatus.CANCELED);
        orderService.update(resumingOrder);

        customerService.setOrderStatus(CustomerProductStatus.ACTIVE);
        LocalDate now  = LocalDate.now();

        Order activationOrder = new Order();
        activationOrder.setCustomerService(customerService);
        activationOrder.setCreationDate(now);
        activationOrder.setExecutionDate(now);
        activationOrder.setOrderStatus(OrderStatus.DONE);
        activationOrder.setType(OrderType.ACTIVATION);

        customerServiceDao.update(customerService);
        orderService.save(activationOrder);

        return customerService;
    }

    @Override
    public CustomerServiceDto suspendCustomerService(Map<String, Object> suspensionData) {
        CustomerServiceDto customerService = customerServiceDao.
                getById((new Long((Integer)suspensionData.get("customerServiceId"))));
        Integer daysToExecution = (Integer) suspensionData.get("daysToExecution");

        customerService.setOrderStatus(CustomerProductStatus.SUSPENDED);

        LocalDate now  = LocalDate.now();
        LocalDate executionDate = now.plusDays(daysToExecution);

        Order suspensionOrder = new Order(customerService, OrderType.SUSPENSION, OrderStatus.DONE, now, now);

        Order resumingOrder = new Order(customerService, OrderType.RESUMING, OrderStatus.PENDING, now, executionDate);

        customerServiceDao.update(customerService);
        orderService.save(suspensionOrder);
        orderService.save(resumingOrder);

        return customerService;
    }
}

