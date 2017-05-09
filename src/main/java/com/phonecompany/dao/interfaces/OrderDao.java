package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Order;

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
    List<Order> getTariffOrdersByRegionId(long regionId);
    List<Order> getAllServiceOrders();
}
