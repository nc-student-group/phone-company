package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.ServiceAlreadyPresentException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.FileService;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.service.interfaces.ServiceService;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends CrudServiceImpl<Service>
        implements ServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceServiceImpl.class);
    public static final double REPRESENTATIVE_DISCOUNT = 0.85;

    private ServiceDao serviceDao;
    private ProductCategoryDao productCategoryDao;
    private FileService fileService;
    private OrderService orderService;
    private CustomerService customerService;

    @Autowired
    public ServiceServiceImpl(ServiceDao serviceDao,
                              ProductCategoryDao productCategoryDao,
                              FileService fileService,
                              OrderService orderService,
                              CustomerService customerService) {
        super(serviceDao);
        this.serviceDao = serviceDao;
        this.productCategoryDao = productCategoryDao;
        this.fileService = fileService;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public Map<String, Object> getServicesByProductCategoryId(long productCategoryId,
                                                              int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<Service> pagedServices = this.serviceDao.getPaging(page, size, productCategoryId);

        List<Service> servicesWithDiscount = this.applyDiscountForServices(pagedServices);

        LOG.debug("Fetched services: {}", servicesWithDiscount);
        response.put("services", servicesWithDiscount);
        response.put("servicesCount", this.serviceDao.getEntityCount(productCategoryId));
        return response;
    }

    private List<Service> applyDiscountForServices(List<Service> services) {
        Customer currentlyLoggedInUser = this.customerService.getCurrentlyLoggedInUser();
        LOG.debug("Currently logged in user: {}", currentlyLoggedInUser);
        if (currentlyLoggedInUser.getRepresentative()) {
            return this.mapServicesToANewPrice(services);
        }
        return services;
    }

    private List<Service> mapServicesToANewPrice(List<Service> services) {
        return services.stream()
                .map(TypeMapper.getDiscountMapper(REPRESENTATIVE_DISCOUNT))
                .collect(Collectors.toList());
    }

    @Override
    public Service save(Service service) {
        Assert.notNull(service, "Service cannot be null");
        if (this.isExist(service)) {
            throw new ServiceAlreadyPresentException(service.getServiceName());
        }
        String pictureUrl = this.getPictureUrlForService(service);
        service.setPictureUrl(pictureUrl);
        String productCategoryName = service.getProductCategory().getCategoryName();
        ProductCategory productCategory = productCategoryDao.getByName(productCategoryName);
        service.setProductCategory(productCategory);
        return super.save(service);
    }

    private String getPictureUrlForService(Service service) {
        String pictureBase64 = service.getPictureUrl();
        LOG.debug("Service base64 picture URL: {}", pictureBase64);
        String pictureUrl = this.fileService.stringToFile(service.getPictureUrl(),
                "service/" + service.hashCode());
        LOG.debug("Picture URL after parsing base64 image representation: {}", pictureUrl);
        return pictureUrl;
    }

    private boolean isExist(Service service) {
        return this.serviceDao.isExist(service);
    }

    @Override
    public void updateServiceStatus(long serviceId, ProductStatus productStatus) {
        this.serviceDao.updateServiceStatus(serviceId, productStatus);
    }

    @Override
    public void activateServiceForUser(User user) {

    }
}
