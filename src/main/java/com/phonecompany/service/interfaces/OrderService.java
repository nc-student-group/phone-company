package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.service.xssfHelper.strategies.GroupingStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public interface OrderService extends CrudService<Order> {

    Order getResumingOrderByCustomerTariff(CustomerTariff customerTariff);

    List<Order> getResumingOrderByCustomerService(CustomerServiceDto customerService);

    List<Order> getOrdersHistoryByClient(Customer customer, int page, int size);

    List<Order> getOrdersHistoryByCorporateId(long corporateId, int page, int size);

    List<Order> getOrdersHistoryForServicesByClient(Customer customer, int page, int size);

    Integer getOrdersCountByClient(Customer customer);

    Integer getOrdersCountForServicesByClient(Customer customer);

    Integer getOrdersCountByCorporateId(long corporateId);

    Order saveCustomerServiceOrder(CustomerServiceDto customerService, OrderType orderType);

    WeeklyOrderStatistics getOrderStatistics();

    List<Statistics> getOrderStatisticsByRegionAndTimePeriod(long regionId,
                                                             LocalDate startDate,
                                                             LocalDate endDate);
}
