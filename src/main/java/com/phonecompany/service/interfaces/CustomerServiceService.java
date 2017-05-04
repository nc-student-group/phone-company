package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;

import java.util.List;

public interface CustomerServiceService extends CrudService<CustomerServiceDto> {

    List<CustomerServiceDto> getCurrentCustomerServices(long customerId);
    List<CustomerServiceDto> getCustomerServicesByCustomerId(long customerId);
    CustomerServiceDto deactivateCustomerService(CustomerServiceDto customerService);
}
