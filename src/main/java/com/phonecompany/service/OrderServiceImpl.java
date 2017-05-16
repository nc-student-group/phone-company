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
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.phonecompany.util.TypeMapper.getStatisticsByOrderTypePredicate;
import static com.phonecompany.util.TypeMapper.getStatisticsByTargetNamePredicate;

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

    @Override
    public List<Order> getServiceOrdersByTimePeriod(LocalDate startDate, LocalDate endDate) {
        return this.orderDao.getServiceOrdersByTimePeriod(startDate, endDate);
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
     * @param statisticsList order list that number will be calculated from
     * @param date           date at which order number should be calculated
     * @return order number
     */
    @Override
    public Long getOrderNumberByDate(List<Statistics> statisticsList,
                                     LocalDate date) {
        Statistics statistics = statisticsList.stream()
                .filter(s -> s.getCreationDate().equals(date))
                .findFirst().orElse(null);
        return Optional.ofNullable(statistics)
                .map(Statistics::getCount)
                .orElse(0L);
    }

    /**
     * Prepares dataset containing information regarding orders of each separate product
     * <p>
     * <p>The resulting dataset is meant to be used in {@link XSSFServiceImpl} in order
     * to create an xls document that depicts an information of the dataset</p>
     *
     * @param sheetName expected sheet name
     * @return constructed sheet dataset
     */
    @Override
    public SheetDataSet<LocalDate, Long> prepareExcelSheetDataSet(String sheetName,
                                                                  List<Statistics> statisticsList) {
        SheetDataSet<LocalDate, Long> sheet = new SheetDataSet<>(sheetName);
        List<OrderType> orderTypes = this.getOrderTypesFromStatistics(statisticsList);
        LOG.debug("Order types: {}", orderTypes);
        for (OrderType orderType : orderTypes) {
            this.populateExcelTableDataSet(sheet, orderType, statisticsList);
        }
        return sheet;
    }

    private List<OrderType> getOrderTypesFromStatistics(List<Statistics> statisticsList) {
        return statisticsList.stream()
                .map(Statistics::getOrderType)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistics> getOrderStatisticsByRegionAndTimePeriod(long regionId,
                                                                    LocalDate startDate,
                                                                    LocalDate endDate) {
        return this.orderDao.getOrderStatisticsByRegionAndTimePeriod(regionId, startDate, endDate);
    }

    /**
     * Populates {@link SheetDataSet} object with its components (e.g. {@link TableDataSet})
     *
     * @param sheet     sheet a corresponding table representation will be created on
     * @param orderType order type that is used to filter out {@code Order} objects
     */
    private void populateExcelTableDataSet(SheetDataSet<LocalDate, Long> sheet,
                                           OrderType orderType,
                                           List<Statistics> statisticsList) {
        TableDataSet<LocalDate, Long> table = sheet.createTable(orderType.toString());
        List<String> uniqueProductNames = this.extractUniqueValues(statisticsList, Statistics::getTargetName);
        List<LocalDate> timeLine = this.generateTimeLine(statisticsList);
        for (String productName : uniqueProductNames) {
            RowDataSet<LocalDate, Long> row = table.createRow(productName);
            //TODO: chain of responsibility pattern?
            Predicate<Statistics> statisticsByTargetNamePredicate = getStatisticsByTargetNamePredicate(productName);
            List<Statistics> statisticsListFilteredByName = this
                    .filterOutStatistics(statisticsList, statisticsByTargetNamePredicate);
            Predicate<Statistics> statisticsByOrderTypePredicate = getStatisticsByOrderTypePredicate(orderType);
            List<Statistics> statisticsListFilteredByType = this
                    .filterOutStatistics(statisticsListFilteredByName, statisticsByOrderTypePredicate);

            this.populateExcelRowDataSet(row, statisticsListFilteredByType, timeLine);
        }
    }

    private List<String> extractUniqueValues(List<Statistics> statisticsList,
                                             Function<Statistics, String> mapper) {
        return statisticsList.stream()
                .map(mapper)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Statistics> filterOutStatistics(List<Statistics> statisticsList,
                                                 Predicate<Statistics> filteringPredicate) {
        return statisticsList
                .stream()
                .filter(filteringPredicate)
                .collect(Collectors.toList());
    }

    /**
     * Populates {@code RowDataSet} object with the cell representations
     *
     * @param row object to be populated with statistical data
     * @param timeLine
     * @see RowDataSet
     */
    private void populateExcelRowDataSet(RowDataSet<LocalDate, Long> row,
                                         List<Statistics> statisticsList, List<LocalDate> timeLine) {
        for (LocalDate date : timeLine) {
            long orderNumberByDate = this.getOrderNumberByDate(statisticsList, date);
            row.addKeyValuePair(date, orderNumberByDate);
        }
    }

    /**
     * Retains {@code Order} objects that correspond only to a specific {@link OrderType}
     * and {@link OrderStatus#DONE}. Other orders would be filtered out.
     *
     * @param orders orders to be filtered
     * @param type   type that is used to filter {@code Order}s by
     * @return filtered list of orders
     */
    @Override
    public List<Order> filterCompletedOrdersByType(List<Order> orders, OrderType type) {
        return orders.stream()
                .filter(o -> o.getType().equals(type))
                .filter(o -> o.getOrderStatus().equals(OrderStatus.DONE))
                .collect(Collectors.toList());
    }

    /**
     * Returns an ordered list of unique creation dates of the orders passed as
     * a parameter.
     *
     * @param orders objects used to retrieve creation dates from
     * @return set of unique dates corresponding to the elements in the incoming list
     */
    @Override
    public List<LocalDate> generateTimeLine(List<Statistics> orders) {
        return orders.stream()
                .map(Statistics::getCreationDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Gets map that contains a lists of {@code Order}s against a {@code String} keys
     * corresponding to these lists
     *
     * @param orders           orders to be put into the map against keys
     * @param groupingStrategy a strategy to be used to associate lists with the
     *                         corresponding keys
     * @return map of order lists against {@code String} keys
     */
    @Override
    public Map<String, List<Order>> getProductNamesToOrdersMap(List<Order> orders,
                                                               GroupingStrategy<Order, String> groupingStrategy) {
        Map<String, List<Order>> productNamesToOrdersMap = new HashMap<>();
        Function<Order, String> tariffOrderToTariffNameMapping = groupingStrategy.getValueToKeyMapper();
        List<String> productNames = this.getDistinctNamesFromOrders(orders, tariffOrderToTariffNameMapping);
        for (String productName : productNames) {
            Predicate<Order> productNameFilter = groupingStrategy.getFilteringCondition(productName);
            List<Order> ordersOfProduct = this.filterOrders(orders, productNameFilter);
            this.putOrdersInMap(productNamesToOrdersMap, productName, ordersOfProduct);
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
     * @return set of unique {@code String} objects
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
