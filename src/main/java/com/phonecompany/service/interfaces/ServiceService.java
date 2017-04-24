package com.phonecompany.service.interfaces;

import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;

import java.util.List;
import java.util.Map;

public interface ServiceService extends CrudService<Service> {
    Map<String, Object> getServicesByProductCategoryId(Long productCategoryId, int page, int size);

    List<Service> getByProductCategoryIdAndPaging(Long productCategoryId, int page, int size);

    Service validateAndSave(Service service);

    Integer getCountByProductCategoryIdAndPaging(long productCategoryId);

    void updateServiceStatus(long serviceId, ProductStatus productStatus);
}
