package com.phonecompany.service.interfaces;

import com.phonecompany.annotations.CacheClear;
import com.phonecompany.annotations.Cacheable;
import com.phonecompany.model.Customer;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.model.paging.PagingResult;

public interface ServiceService extends CrudService<Service> {

    PagingResult<Service> getServicesByProductCategoryId(int page, int size,
                                                         int productCategoryId);
    Service save(Service service);

    void updateServiceStatus(long serviceId, ProductStatus productStatus);

    void activateServiceForCustomer(Service service, Customer customer);

    Service getById(Long id);
}
