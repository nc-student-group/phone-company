package com.phonecompany.service;

import com.phonecompany.annotations.CacheClear;
import com.phonecompany.annotations.Cacheable;
import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.exception.EmptyResultSetException;
import com.phonecompany.exception.ServiceAlreadyPresentException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.model.paging.PagingResult;
import com.phonecompany.service.interfaces.*;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.util.Query;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ServiceStereotype
public class ServiceServiceImpl extends CrudServiceImpl<Service>
        implements ServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceServiceImpl.class);
    public static final double REPRESENTATIVE_DISCOUNT = 15;

    private ServiceDao serviceDao;
    private ProductCategoryDao productCategoryDao;
    private FileService fileService;
    private CustomerService customerService;
    private OrderService orderService;
    private StatisticsService<LocalDate, Long> statisticsService;

    @Autowired
    public ServiceServiceImpl(ServiceDao serviceDao,
                              ProductCategoryDao productCategoryDao,
                              FileService fileService,
                              CustomerService customerService,
                              OrderService orderService,
                              StatisticsService<LocalDate, Long> statisticsService) {
        super(serviceDao);
        this.serviceDao = serviceDao;
        this.productCategoryDao = productCategoryDao;
        this.fileService = fileService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.statisticsService = statisticsService;
    }

    @Override
    @Cacheable
    public PagingResult<Service> getServicesByProductCategoryId(int page, int size,
                                                                int firstFilter) {

        List<Service> pagedServices = this.serviceDao.getPaging(page, size, firstFilter);
        List<Service> servicesWithDiscount = this.applyDiscount(pagedServices);
        LOG.debug("Services to be put in response: {}", servicesWithDiscount);

        int serviceEntityCount = this.serviceDao.getEntityCount(firstFilter);

        PagingResult<Service> pagingResult = new PagingResult<>(servicesWithDiscount, serviceEntityCount);
        LOG.debug("Paging result to be returned: {}", pagingResult);
        return pagingResult;
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
    @Cacheable
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

    @Override
    @CacheClear
    public Service update(Service service) {
        Assert.notNull(service, "Service cannot be null");
        LOG.debug("Input service picture url: {}", service.getPictureUrl());
        String pictureUrl = this.getPictureUrlForService(service);
        service.setPictureUrl(pictureUrl);
        LOG.debug("New picture url: {}", pictureUrl);
        return super.update(service);
    }

    @Override
    @CacheClear
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

    private boolean isExist(Service service) {
        return this.serviceDao.isExist(service);
    }

    private String getPictureUrlForService(Service service) {
        String pictureBase64 = service.getPictureUrl();
        LOG.debug("Service base64 picture URL: {}", pictureBase64);
        String pictureUrl = this.fileService.stringToFile(service.getPictureUrl(),
                "service/" + service.hashCode());
        LOG.debug("Picture URL after parsing base64 image representation: {}", pictureUrl);
        return pictureUrl;
    }

    @Override
    @CacheClear
    public void updateServiceStatus(long serviceId, ProductStatus productStatus) {
        this.serviceDao.updateServiceStatus(serviceId, productStatus);
    }

    @Override
    public SheetDataSet<LocalDate, Long> getServiceStatisticsDataSet(LocalDate startDate, LocalDate endDate) {
        List<Statistics> statisticsList = this.orderService.getServiceOrderStatisticsByTimePeriod(startDate, endDate);
        if (statisticsList.size() == 0) {
            throw new EmptyResultSetException("There were no tariff orders in this region during " +
                    "this period");
        }
        return statisticsService
                .prepareStatisticsDataSet("Services", statisticsList,
                        startDate, endDate);
    }

    @Override
    @Cacheable
    public List<Service> getAllServicesSearch(int page, int size,String name, String status, int lowerPrice, int upperPrice) {
        Query.Builder query = new Query.Builder("service");
        query.where();
        query.addLikeCondition("service_name",name);
        query.and().addCondition("price > ?",lowerPrice);
        query.and().addCondition("price < ?",upperPrice);
        if(status.equals("ACTIVATED") || status.equals("DEACTIVATED")){
            query.and().addCondition("product_status = ?",status);
        }else if(!status.equals("-")){
            throw new ConflictException("Incorrect parameter: service status");
        }
        query.addPaging(page,size);
        return serviceDao.getAllServicesSearch(query.build());
    }

    @Override
    public int getCountSearch(int page, int size, String name, String status, int lowerPrice, int upperPrice) {
        Query.Builder query = new Query.Builder("service");
        query.where();
        query.addLikeCondition("service_name",name);
        query.and().addCondition("price > ?",lowerPrice);
        query.and().addCondition("price < ?",upperPrice);
        if(status.equals("ACTIVATED") || status.equals("DEACTIVATED")){
            query.and().addCondition("product_status = ?",status);
        }else if(!status.equals("-")){
            throw new ConflictException("Incorrect parameter: service status");
        }
        return serviceDao.getAllServicesSearch(query.build()).size();
    }
}
