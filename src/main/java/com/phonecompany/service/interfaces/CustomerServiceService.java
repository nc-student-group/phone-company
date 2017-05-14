package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;

import java.util.List;
import java.util.Map;

public interface CustomerServiceService extends CrudService<CustomerServiceDto> {

    List<CustomerServiceDto> getCurrentCustomerServices(long customerId);
    List<CustomerServiceDto> getCustomerServicesByCustomerId(long customerId);
    CustomerServiceDto deactivateCustomerService(CustomerServiceDto customerService);
    CustomerServiceDto resumeCustomerService(CustomerServiceDto customerService);
    CustomerServiceDto suspendCustomerService(Map<String, Object> suspensionData);
    CustomerServiceDto activateServiceForCustomer(long serviceId, Customer customer);
}
