package com.phonecompany.service;

import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderServiceImpl extends CrudServiceImpl<Order>
        implements OrderService {

    private OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao){
        super(orderDao);
        this.orderDao = orderDao;
    }

    // CustomerService name changed to CustomerServiceDto because of the name collision
    public Order saveCustomerServiceActivationOrder(CustomerServiceDto customerService) {
        LocalDate currentDate = LocalDate.now();
        Order order =
                new Order(customerService, OrderType.ACTIVATION,
                        OrderStatus.CREATED, currentDate, currentDate);
        return super.save(order);
    }
}
