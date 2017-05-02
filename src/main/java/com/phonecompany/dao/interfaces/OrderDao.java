package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Order;

import java.util.List;

public interface OrderDao extends CrudDao<Order> {

    List<Order> getResumingOrderByCustomerTariffId(Long customerId);
}
