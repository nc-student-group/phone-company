package com.phonecompany.service.interfaces;

import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.model.paging.PagingResult;

import java.util.List;

import java.time.LocalDate;

public interface ServiceService extends CrudService<Service>,
        SimpleStatisticsGenerating<LocalDate, Long> {

    PagingResult<Service> getServicesByProductCategoryId(int page, int size,
                                                         int productCategoryId);

    Service save(Service service);

    void updateServiceStatus(long serviceId, ProductStatus productStatus);

    Service getById(Long id);

    List<Service> getAllServicesSearch(int page, int size,String name,String  status,int lowerPrice,int upperPrice);
    int getCountSearch(int page, int size,String name,String  status,int lowerPrice,int upperPrice);
}
