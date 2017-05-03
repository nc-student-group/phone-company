package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.ServiceAlreadyPresentException;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.*;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends CrudServiceImpl<Service>
        implements ServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceServiceImpl.class);
    public static final double REPRESENTATIVE_DISCOUNT = 15;

    private ServiceDao serviceDao;
    private ProductCategoryDao productCategoryDao;
    private FileService fileService;
    private OrderService orderService;
    private CustomerService customerService;
    private CustomerServiceService customerServiceService;

    @Autowired
    public ServiceServiceImpl(ServiceDao serviceDao,
                              ProductCategoryDao productCategoryDao,
                              FileService fileService,
                              OrderService orderService,
                              CustomerService customerService,
                              CustomerServiceService customerServiceService) {
        super(serviceDao);
        this.serviceDao = serviceDao;
        this.productCategoryDao = productCategoryDao;
        this.fileService = fileService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.customerServiceService = customerServiceService;
    }

    @Override
    public Map<String, Object> getServicesByProductCategoryId(long productCategoryId,
                                                              int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<Service> pagedServices = this.serviceDao.getPaging(page, size, productCategoryId);

        List<Service> servicesWithDiscount = this.applyDiscount(pagedServices);

        LOG.debug("Fetched services: {}", servicesWithDiscount);
        response.put("services", servicesWithDiscount);
        response.put("servicesCount", this.serviceDao.getEntityCount(productCategoryId));

        return response;
    }

    @Override
    public Service getById(Long serviceId) {
        Service serviceFoundById = super.getById(serviceId);
        LOG.debug("Service price without a discount: {}", serviceFoundById.getPrice());
        Service serviceWithDiscount = this.applyDiscount(serviceFoundById);
        LOG.debug("Service price after applying discount: {}", serviceWithDiscount.getPrice());
        return serviceWithDiscount;
    }

    private Service applyDiscount(Service service) {
        Customer currentlyLoggedInUser = this.customerService.getCurrentlyLoggedInUser();
        LOG.debug("Currently logged in user: {}", currentlyLoggedInUser);
        if (currentlyLoggedInUser.getRepresentative()) {
            return TypeMapper.getDiscountMapper(REPRESENTATIVE_DISCOUNT).apply(service);
        }
        return service;
    }

    private List<Service> applyDiscount(List<Service> services) {
        Customer currentlyLoggedInUser = this.customerService.getCurrentlyLoggedInUser();
        LOG.debug("Currently logged in user: {}", currentlyLoggedInUser);
        if (currentlyLoggedInUser.getRepresentative()) {
            return this.mapToANewPrice(services);
        }
        return services;
    }

    private List<Service> mapToANewPrice(List<Service> services) {
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
    public void activateServiceForCustomer(long serviceId, Customer customer) {
        Service currentService = this.getById(serviceId);
        CustomerServiceDto customerService =
                new CustomerServiceDto(customer, currentService,
                        currentService.getPrice(), CustomerProductStatus.ACTIVE);
        this.orderService.saveCustomerServiceActivationOrder(customerService);
        this.customerServiceService.save(customerService);
    }
}
