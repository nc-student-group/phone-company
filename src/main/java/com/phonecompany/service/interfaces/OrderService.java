package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.Order;

import java.util.List;

public interface OrderService extends CrudService<Order>{

    Order getResumingOrderByCustomerTariff(CustomerTariff customerTariff);
    Order getResumingOrderByCustomerService(CustomerServiceDto customerService);
    Order saveCustomerServiceActivationOrder(CustomerServiceDto customerServiceDto);
    List<Order> getOrdersHistoryByClient(Customer customer, int page, int size);
    List<Order> getOrdersHistoryForServicesByClient(Customer customer, int page, int size);
    Integer getOrdersCountByClient(Customer customer);
    Integer getOrdersCountForServicesByClient(Customer customer);
}
