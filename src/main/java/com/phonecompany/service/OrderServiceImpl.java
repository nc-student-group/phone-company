package com.phonecompany.service;

import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.service.xssfHelper.RowDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.TableDataSet;
import com.phonecompany.service.xssfHelper.GroupingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

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

    private List<Order> filterOrdersByDate(List<Order> orderList,
                                           LocalDate startDate, LocalDate endDate) {
        return orderList.stream()
                .filter(t -> t.getCreationDate().isAfter(startDate) ||
                        t.getCreationDate().isEqual(startDate))
                .filter(t -> t.getCreationDate().isBefore(endDate) ||
                        t.getCreationDate().isEqual(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Gets the number of orders made at the specified date.
     *
     * @param orderList order list that number will be calculated from
     * @param date
     * @return
     */
    @Override
    public Long getOrderNumberByDate(List<Order> orderList,
                                     LocalDate date) {
        return orderList.stream()
                .filter(o -> o.getCreationDate().equals(date))
                .count();
    }

    @Override
    public SheetDataSet prepareExcelSheetDataSet(String sheetName,
                                                 Map<String, List<Order>> productNamesToOrdersMap,
                                                 List<LocalDate> timeLine) {
        SheetDataSet<Long, LocalDate> sheet = new SheetDataSet<>(sheetName);
        List<OrderType> orderTypes = asList(OrderType.ACTIVATION, OrderType.DEACTIVATION);
        for (OrderType orderType : orderTypes) {
            this.prepareExcelTableDataSet(sheet, orderType, productNamesToOrdersMap, timeLine);
        }
        return sheet;
    }

    private void prepareExcelTableDataSet(SheetDataSet<Long, LocalDate> sheet, OrderType orderType,
                                          Map<String, List<Order>> productNamesToOrdersMap,
                                          List<LocalDate> timeLine) {
        TableDataSet<Long, LocalDate> table = sheet.createTable(orderType.toString());
        for (String productName : productNamesToOrdersMap.keySet()) {
            this.prepareExcelRowDataSet(table, productName, orderType,
                    productNamesToOrdersMap, timeLine);
        }
    }

    private void prepareExcelRowDataSet(TableDataSet<Long, LocalDate> table,
                                        String productName,
                                        OrderType orderType,
                                        Map<String, List<Order>> productNamesToOrdersMap,
                                        List<LocalDate> timeLine) {
        RowDataSet<Long, LocalDate> row = table.createRow(productName);
        List<Order> orders = productNamesToOrdersMap.get(productName);
        List<Order> ordersByType = this.filterCompletedOrdersByType(orders, orderType);
        for (LocalDate date : timeLine) {
            long orderNumberByDate = this.getOrderNumberByDate(ordersByType, date);
            row.addKeyValuePair(orderNumberByDate, date);
        }
    }

    /**
     * @param orders
     * @param type
     * @return
     */
    @Override
    public List<Order> filterCompletedOrdersByType(List<Order> orders, OrderType type) {
        return orders.stream()
                .filter(o -> o.getType().equals(type))
                .filter(o -> o.getOrderStatus().equals(OrderStatus.DONE))
                .collect(Collectors.toList());
    }

    /**
     * Returns a set of unique creation dates of the orders passed as a parameter.
     * The returned list will be sorted in ascending order.
     * <p>
     * <p>Resulting list is made ordered because this method is generally used to
     * produce range of definition for the values contained within an xls report.</p>
     *
     * @param orders objects used to retrieve creation dates from
     * @return set of unique dates corresponding to the elements in the incoming list
     */
    @Override
    public List<LocalDate> generateTimeLine(List<Order> orders) {
        return orders.stream()
                .map(Order::getCreationDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Order>> getProductNamesToOrdersMap(List<Order> orders,
                                                               GroupingStrategy<Order, String> filteringStrategy) {
        Map<String, List<Order>> productNamesToOrdersMap = new HashMap<>();
        Function<Order, String> tariffOrderToTariffNameMapping = filteringStrategy.getValueToKeyMapper();
        List<String> productNames = this.getDistinctNamesFromOrders(orders, tariffOrderToTariffNameMapping);
        for (String productName : productNames) {
            Predicate<Order> productNameFilter = filteringStrategy.getFilteringCondition(productName);
            List<Order> ordersOfTariff = this.filterOrders(orders, productNameFilter);
            this.putOrdersInMap(productNamesToOrdersMap, productName, ordersOfTariff);
        }
        return productNamesToOrdersMap;
    }

    /**
     * Returns a set of unique strings that somehow relate to each {@code Order} object
     * from the incoming list. Example: order -> tariff name this order was made for.
     *
     * @param orders     {@code Order} objects to be mapped to the {@code String} keys
     * @param nameMapper mapper that is used to map {@code Order} object to the
     *                   corresponding {@code String} key
     * @return set of unique {@code String}s
     */
    private List<String> getDistinctNamesFromOrders(List<Order> orders,
                                                    Function<Order, String> nameMapper) {
        return orders
                .stream().map(nameMapper)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Filters list of orders depending on the filtering condition.
     *
     * @param orders             list of orders to be filtered
     * @param filteringCondition predicate used to filter orders
     * @return filtered list
     */
    private List<Order> filterOrders(List<Order> orders,
                                     Predicate<Order> filteringCondition) {
        return orders.stream()
                .filter(filteringCondition)
                .collect(Collectors.toList());
    }

    /**
     * Places a list of {@code Order}s into the destination map against a
     * {@code String} key corresponding to this list.
     * <p>
     * <p>If the given key was already put into the map then the incoming list
     * and the existent list will be merged.</p>
     *
     * @param destinationMap  map that list would be placed into
     * @param key             key that corresponds to the incoming list
     * @param ordersOfProduct list to be placed into the map
     */
    private void putOrdersInMap(Map<String, List<Order>> destinationMap,
                                String key, List<Order> ordersOfProduct) {
        if (destinationMap.get(key) == null) {
            List<Order> orders = new ArrayList<>();
            orders.addAll(ordersOfProduct);
            destinationMap.put(key, orders);
        } else {
            destinationMap.get(key).addAll(ordersOfProduct);
        }
    }
}
