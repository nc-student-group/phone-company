package com.phonecompany.service.interfaces;

import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;

public interface OrderService extends CrudService<Order>{

    Order getResumingOrderByCustomerTariff(CustomerTariff customerTariff);

}
