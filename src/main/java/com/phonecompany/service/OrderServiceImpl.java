package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.service.xssfHelper.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

@ServiceStereotype
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
        return orderDao.getResumingOrderByCustomerTariffId(customerTariff.getId());
    }

    @Override
    public List<Order> getResumingOrderByCustomerService(CustomerServiceDto customerService) {
        return orderDao.getResumingOrderByCustomerServiceId(customerService.getId());
    }

    @Override
    public Order saveCustomerServiceOrder(CustomerServiceDto customerService, OrderType orderType) {
        LocalDate currentDate = LocalDate.now();
        Order order =
                new Order(customerService, orderType,
                        OrderStatus.CREATED, currentDate, currentDate);
        return super.save(order);
    }

    @Override
    public List<Order> getOrdersHistoryByClient(Customer customer, int page, int size) {
        Long clientId = customer.getId();
        return customer.getRepresentative() ? orderDao.getOrdersByCorporateIdPaged(customer.getCorporate().getId(),
                page, size) : orderDao.getOrdersByCustomerIdPaged(clientId, page, size);
    }

    @Override
    public List<Order> getOrdersHistoryByCorporateId(long corporateId, int page, int size) {
        return orderDao.getOrdersByCorporateIdPaged(corporateId, page, size);
    }

    @Override
    public Integer getOrdersCountByClient(Customer customer) {
        Long clientId = customer.getId();
        return customer.getRepresentative() ? this.getOrdersCountByCorporateId(customer.getCorporate().getId()) :
                orderDao.getCountByCustomerId(clientId);
    }

    @Override
    public Integer getOrdersCountByCorporateId(long corporateId) {
        return orderDao.getCountByCorporateId(corporateId);
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
    public WeeklyOrderStatistics getOrderStatistics() {

        EnumMap<WeekOfMonth, Integer> numberOfActivationOrdersForTheLastMonth =
                this.orderDao.getNumberOfOrdersForTheLastMonthByType(OrderType.ACTIVATION);
        EnumMap<WeekOfMonth, Integer> numberOfDeactivationOrdersForTheLastMonth =
                this.orderDao.getNumberOfOrdersForTheLastMonthByType(OrderType.DEACTIVATION);

        return new WeeklyOrderStatistics(numberOfDeactivationOrdersForTheLastMonth,
                numberOfActivationOrdersForTheLastMonth);
    }

    @Override
    public List<Statistics> getTariffOrderStatisticsByRegionAndTimePeriod(long regionId,
                                                                          LocalDate startDate,
                                                                          LocalDate endDate) {
        return this.orderDao.getTariffsOrderStatisticsByRegionAndTimePeriod(regionId, startDate, endDate);
    }

    @Override
    public List<Statistics> getServiceOrderStatisticsByTimePeriod(LocalDate startDate, LocalDate endDate) {
        return this.orderDao.getServicesOrderStatisticsByTimePeriod(startDate, endDate);
    }

    @Override
    public Order getNextResumingOrder() {
        return this.orderDao.getNextResumingOrder();
    }

}
