package com.phonecompany.dao.interfaces;

import com.phonecompany.model.CustomerServiceDto;

import java.util.List;

public interface CustomerServiceDao extends JdbcOperations<CustomerServiceDto> {

    List<CustomerServiceDto> getCurrentCustomerServices(long customerId);
    List<CustomerServiceDto> getCustomerServicesByCustomerId(long customerId);
    boolean isCustomerServiceAlreadyPresent(long serviceId, long customerId);
}
