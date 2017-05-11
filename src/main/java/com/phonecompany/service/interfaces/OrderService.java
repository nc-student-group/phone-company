package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;
import com.phonecompany.service.FilteringStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public interface OrderService extends CrudService<Order> {

    Order getResumingOrderByCustomerTariff(CustomerTariff customerTariff);

    List<Order> getResumingOrderByCustomerService(CustomerServiceDto customerService);

    Order saveCustomerServiceActivationOrder(CustomerServiceDto customerServiceDto);

    List<Order> getOrdersHistoryByClient(Customer customer, int page, int size);

    List<Order> getOrdersHistoryByCorporateId(long corporateId, int page, int size);

    List<Order> getOrdersHistoryForServicesByClient(Customer customer, int page, int size);

    Integer getOrdersCountByClient(Customer customer);

    Integer getOrdersCountForServicesByClient(Customer customer);

    Integer getOrdersCountByCorporateId(long corporateId);

    OrderStatistics getOrderStatistics();

    List<Order> getTariffOrdersByRegionIdAndTimePeriod(long regionId, LocalDate startDate, LocalDate endDate);

    Map<String, List<Order>> getProductNamesToOrdersMap(List<Order> orders,
                                                        FilteringStrategy filteringStrategy);
}
