package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;

import java.util.List;

public interface ServiceDao extends CrudDao<Service> {
    List<Service> getByProductCategoryIdAndPaging(Long productCategoryId, int page, int size);

    boolean isExist(Service service);

    Integer getCountByProductCategoryIdAndPaging(long regionId);

    void updateServiceStatus(long serviceId, ProductStatus productStatus);
}
