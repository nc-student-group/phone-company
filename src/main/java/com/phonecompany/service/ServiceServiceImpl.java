package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.ServiceAlreadyPresentException;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.FileService;
import com.phonecompany.service.interfaces.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends CrudServiceImpl<Service>
        implements ServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private ServiceDao serviceDao;
    private ProductCategoryDao productCategoryDao;
    private FileService fileService;

    @Autowired
    public ServiceServiceImpl(ServiceDao serviceDao,
                              ProductCategoryDao productCategoryDao,
                              FileService fileService) {
        super(serviceDao);
        this.serviceDao = serviceDao;
        this.productCategoryDao = productCategoryDao;
        this.fileService = fileService;
    }

    @Override
    public Map<String, Object> getServicesByProductCategoryId(Long productCategoryId, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<Service> services = this.serviceDao.getPaging(page, size, productCategoryId);

        LOG.debug("Fetched services: {}", services);
        response.put("services", services);
        response.put("servicesCount", this.serviceDao.getEntityCount(productCategoryId));
        return response;
    }

    @Override
    public Service validateAndSave(Service service) {
        Assert.notNull(service, "Service cannot be null");
        if(this.isExist(service)) {
            throw new ServiceAlreadyPresentException(service.getServiceName());
        }
        String pictureBase64 = service.getPictureUrl();
        LOG.debug("Service base64 picture URL: {}", pictureBase64);
        String pictureUrl = this.fileService.stringToFile(service.getPictureUrl(), "service/" + LocalDate.now().hashCode());
        LOG.debug("Picture URL after parsing base64 image representation: {}", pictureUrl);
        service.setPictureUrl(pictureUrl);
        String productCategoryName = service.getProductCategory().getCategoryName();
        ProductCategory productCategory = productCategoryDao.getByName(productCategoryName);
        service.setProductCategory(productCategory);
        return super.save(service);
    }

    private boolean isExist(Service service) {
        return this.serviceDao.isExist(service);
    }

    @Override
    public void updateServiceStatus(long serviceId, ProductStatus productStatus) {
        this.serviceDao.updateServiceStatus(serviceId, productStatus);
    }
}
