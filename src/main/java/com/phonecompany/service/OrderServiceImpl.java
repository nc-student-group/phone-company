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

import java.util.*;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
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
        return orderDao.getResumingOrderByCustomerTariffId(customerTariff.getId());
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
    public OrderStatistics getOrderStatistics() {

        EnumMap<WeekOfMonth, Integer> numberOfActivationOrdersForTheLastMonth =
                this.orderDao.getNumberOfOrdersForTheLastMonthByType(OrderType.ACTIVATION);
        EnumMap<WeekOfMonth, Integer> numberOfDeactivationOrdersForTheLastMonth =
                this.orderDao.getNumberOfOrdersForTheLastMonthByType(OrderType.DEACTIVATION);

        return new OrderStatistics(numberOfDeactivationOrdersForTheLastMonth,
                numberOfActivationOrdersForTheLastMonth);
    }

    @Override
    public List<Order> getTariffOrdersByRegionIdAndTimePeriod(long regionId,
                                                              LocalDate startDate,
                                                              LocalDate endDate) {
        return this.filterOrdersByDate(
                this.orderDao.getTariffOrdersByRegionId(regionId), startDate, endDate);
    }

    @Override
    public Map<String, List<Order>> getProductNamesToOrdersMap(List<Order> orders,
                                                               FilteringStrategy filteringStrategy) {
        Map<String, List<Order>> productNamesToOrdersMap = new HashMap<>();
        Function<Order, String> tariffOrderToTariffNameMapping = filteringStrategy.getOrderToProductNameMapper();
        List<String> productNames = this.getDistinctNamesFromOrders(orders, tariffOrderToTariffNameMapping);
        for (String productName : productNames) {
            Predicate<Order> productNameFilter = filteringStrategy.getProductNameFilter(productName);
            List<Order> ordersOfTariff = this.filterOrders(orders, productNameFilter);
            this.putOrdersInMap(productNamesToOrdersMap, productName, ordersOfTariff);
        }
        return productNamesToOrdersMap;
    }

    private List<Order> filterOrders(List<Order> orders,
                                     Predicate<Order> filterExpression) {
        return orders.stream()
                .filter(filterExpression)
                .collect(Collectors.toList());
    }

    private void putOrdersInMap(Map<String, List<Order>> map,
                                String key, List<Order> ordersOfProduct) {
        if (map.get(key) == null) {
            List<Order> orders = new ArrayList<>();
            orders.addAll(ordersOfProduct);
            map.put(key, orders);
        } else {
            map.get(key).addAll(ordersOfProduct);
        }
    }
    private List<String> getDistinctNamesFromOrders(List<Order> tariffOrders,
                                                    Function<Order, String> nameMapper) {
        return tariffOrders
                .stream().map(nameMapper)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Order> filterOrdersByDate(List<Order> orderList,
                                           LocalDate startDate, LocalDate endDate) {
        return orderList.stream()
                .filter(t -> t.getCreationDate().isAfter(startDate) ||
                        t.getCreationDate().isEqual(startDate))
                .filter(t -> t.getCreationDate().isBefore(endDate) ||
                        t.getCreationDate().isEqual(endDate))
                .collect(Collectors.toList());
    }
}
