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

    /*
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
    * */
}

