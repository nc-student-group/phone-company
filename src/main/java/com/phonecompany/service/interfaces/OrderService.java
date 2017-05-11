package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.xssfHelper.ExcelRow;
import com.phonecompany.service.xssfHelper.ExcelSheet;
import com.phonecompany.service.xssfHelper.ExcelTable;
import com.phonecompany.service.xssfHelper.FilteringStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@SuppressWarnings("SameParameterValue")
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

    Map<String, List<Order>> getProductNamesToOrdersMap(List<Order> orders, FilteringStrategy filteringStrategy);

    List<LocalDate> generateTimeLine(List<Order> orders);

    List<Order> filterCompletedOrdersByType(List<Order> orders, OrderType type);

    long getOrderNumberByDate(List<Order> orderList, LocalDate date);

    ExcelSheet prepareExcelSheetDataset(String sheetName,
                                        Map<String, List<Order>> productNamesToOrdersMap,
                                        List<LocalDate> timeLine);
}
