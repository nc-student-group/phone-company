package com.phonecompany.dao.interfaces;

import com.phonecompany.model.CustomerServiceDto;

import java.util.List;

public interface CustomerServiceDao extends CrudDao<CustomerServiceDto> {

    List<CustomerServiceDto> getCurrentCustomerServices(long customerId);
    List<CustomerServiceDto> getCustomerServicesByCustomerId(long customerId);

}
