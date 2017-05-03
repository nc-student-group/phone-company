package com.phonecompany.service;

import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends CrudServiceImpl<Order> implements OrderService {

    private OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao){
        super(orderDao);
        this.orderDao = orderDao;
    }

    @Override
    public Order getResumingOrderByCustomerTariff(CustomerTariff customerTariff) {
        return orderDao.getResumingOrderByCustomerTariffId(customerTariff.getId()).stream().
                filter(o -> OrderStatus.PENDING.equals(o.getOrderStatus()))
                .collect(Collectors.toList()).get(0);
    }
}
