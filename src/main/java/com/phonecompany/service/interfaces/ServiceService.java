package com.phonecompany.service.interfaces;

import com.phonecompany.model.Service;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.ProductStatus;

import java.util.List;
import java.util.Map;

public interface ServiceService extends CrudService<Service> {
    Map<String, Object> getServicesByProductCategoryId(long productCategoryId,
                                                       int page, int size);

    Service save(Service service);

    void updateServiceStatus(long serviceId, ProductStatus productStatus);

    void activateServiceForUser(User user);
}
