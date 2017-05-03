package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;

import java.util.Map;

public interface ServiceService extends CrudService<Service> {
    Map<String, Object> getServicesByProductCategoryId(long productCategoryId,
                                                       int page, int size);
    Service save(Service service);

    void updateServiceStatus(long serviceId, ProductStatus productStatus);

    void activateServiceForCustomer(long serviceId, Customer customer);

    Service getById(Long id);
}
