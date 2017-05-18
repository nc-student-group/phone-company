package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Order;
import com.phonecompany.model.OrderStatistics;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.service.xssfHelper.Statistics;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

public interface OrderDao extends CrudDao<Order> {

    Order getResumingOrderByCustomerTariffId(Long customerId);
    List<Order> getResumingOrderByCustomerServiceId(Long customerId);
    List<Order> getOrdersByCustomerIdPaged(Long customerId, int page, int size);
    List<Order> getOrdersByCorporateIdPaged(Long corporate, int page, int size);
    List<Order> getOrdersForCustomerServicesByCustomerIdPaged(Long customerId, int page, int size);
    Integer getCountByCustomerId(Long customerId);
    Integer getCountByCorporateId(Long corporateId);
    Integer getCountOfServicesByCustomerId(Long customerId);

    EnumMap<WeekOfMonth, Integer> getNumberOfOrdersForTheLastMonthByType(OrderType type);
    List<Order> getTariffOrdersByRegionId(long regionId);
    List<Order> getServiceOrdersByTimePeriod(LocalDate startDate, LocalDate endDate);

    List<Statistics> getOrderStatisticsByRegionAndTimePeriod(long regionId, LocalDate startDate, LocalDate endDate);
}
