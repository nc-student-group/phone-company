package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.model.paging.PagingResult;

import java.util.Map;

public interface ServiceService extends CrudService<Service> {
    PagingResult<Service> getServicesByProductCategoryId(int productCategoryId,
                                                         int page, int size);
    Service save(Service service);

    void updateServiceStatus(long serviceId, ProductStatus productStatus);

    void activateServiceForCustomer(Service service, Customer customer);

    Service getById(Long id);
}
