package com.phonecompany.service;

import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.service.interfaces.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;

@Service
public class OrderServiceImpl extends CrudServiceImpl<Order>
        implements OrderService {

    private OrderDao orderDao;

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        super(orderDao);
        this.orderDao = orderDao;
    }

    @Override
    public Order getResumingOrderByCustomerTariff(CustomerTariff customerTariff) {
        return orderDao.getResumingOrderByCustomerTariffId(customerTariff.getId()).stream().
                filter(o -> OrderStatus.PENDING.equals(o.getOrderStatus()))
                .collect(Collectors.toList()).get(0);
    }

    @Override
    public List<Order> getResumingOrderByCustomerService(CustomerServiceDto customerService) {
        //?????????????????????
//        return orderDao.getResumingOrderByCustomerServiceId(customerService.getId()).stream().
//                filter(o -> OrderStatus.PENDING.equals(o.getOrderStatus()))
//                .collect(Collectors.toList()).get(0);
        return orderDao.getResumingOrderByCustomerServiceId(customerService.getId());
    }

    public Order saveCustomerServiceActivationOrder(CustomerServiceDto customerService) {
        LocalDate currentDate = LocalDate.now();
        Order order =
                new Order(customerService, OrderType.ACTIVATION,
                        OrderStatus.CREATED, currentDate, currentDate);
        return super.save(order);
    }

    @Override
    public List<Order> getOrdersHistoryByClient(Customer customer, int page, int size) {
        Long clientId = customer.getId();
        return customer.getRepresentative() ? orderDao.getOrdersByCorporateIdPaged(clientId, page, size) :
                orderDao.getOrdersByCustomerIdPaged(clientId, page, size);
    }

    @Override
    public Integer getOrdersCountByClient(Customer customer) {
        Long clientId = customer.getId();
        return customer.getRepresentative() ? orderDao.getCountByCorporateId(clientId) :
                orderDao.getCountByCustomerId(clientId);
    }

    @Override
    public List<Order> getOrdersHistoryForServicesByClient(Customer customer, int page, int size) {
        return orderDao.getOrdersForCustomerServicesByCustomerIdPaged(customer.getId(), page, size);
    }

    @Override
    public Integer getOrdersCountForServicesByClient(Customer customer) {
        return orderDao.getCountOfServicesByCustomerId(customer.getId());
    }

    @Override
    public OrderStatistics getOrderStatistics() {
        EnumMap<WeekOfMonth, Integer> numberOfActivationOrdersForTheLastMonth =
                this.orderDao.getNumberOfOrdersForTheLastMonthByType(OrderType.ACTIVATION);
        LOG.debug("numberOfActivationOrdersForTheLastMonth: {}", numberOfActivationOrdersForTheLastMonth);
        EnumMap<WeekOfMonth, Integer> numberOfDeactivationOrdersForTheLastMonth =
                this.orderDao.getNumberOfOrdersForTheLastMonthByType(OrderType.DEACTIVATION);
        LOG.debug("numberOfDeactivationOrdersForTheLastMonth: {}", numberOfDeactivationOrdersForTheLastMonth);
        return new OrderStatistics(numberOfDeactivationOrdersForTheLastMonth,
                numberOfActivationOrdersForTheLastMonth);
    }
}
