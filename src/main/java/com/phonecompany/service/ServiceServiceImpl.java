package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.ServiceAlreadyPresentException;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.service.interfaces.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends CrudServiceImpl<Service>
        implements ServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private ServiceDao serviceDao;
    private ProductCategoryDao productCategoryDao;

    @Autowired
    public ServiceServiceImpl(ServiceDao serviceDao,
                              ProductCategoryDao productCategoryDao) {
        super(serviceDao);
        this.serviceDao = serviceDao;
        this.productCategoryDao = productCategoryDao;
    }

    @Override
    public Map<String, Object> getServicesByProductCategoryId(Long productCategoryId, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<Service> services = this.getByProductCategoryIdAndPaging(productCategoryId, page, size);

        LOG.debug("Fetched services: {}", services);
        response.put("services", services);
        response.put("servicesCount", this.getCountByProductCategoryIdAndPaging(productCategoryId));
        return response;
    }

    @Override
    public Integer getCountByProductCategoryIdAndPaging(long productCategoryId) {
        return this.serviceDao.getCountByProductCategoryIdAndPaging(productCategoryId);
    }

    @Override
    public List<Service> getByProductCategoryIdAndPaging(Long productCategoryId, int page, int size) {
        return this.serviceDao.getByProductCategoryIdAndPaging(productCategoryId, page, size);
    }

    @Override
    public Service validateAndSave(Service service) {
        Assert.notNull(service, "Service cannot be null");
        if(this.isExist(service)) {
            throw new ServiceAlreadyPresentException(service.getServiceName());
        }
        String productCategoryName = service.getProductCategory().getCategoryName();
        ProductCategory productCategory = productCategoryDao.getByName(productCategoryName);
        service.setProductCategory(productCategory);
        return super.save(service);
    }

    private boolean isExist(Service service) {
        return this.serviceDao.isExist(service);
    }
}
