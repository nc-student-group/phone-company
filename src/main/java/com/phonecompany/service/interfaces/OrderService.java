package com.phonecompany.service.interfaces;

import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.Order;

public interface OrderService extends CrudService<Order>{
    Order saveCustomerServiceActivationOrder(CustomerServiceDto customerServiceDto);
}
