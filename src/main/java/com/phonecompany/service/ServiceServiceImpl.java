package com.phonecompany.service;

import com.phonecompany.annotations.CacheClear;
import com.phonecompany.annotations.Cacheable;
import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.exception.service_layer.MissingResultException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.model.paging.PagingResult;
import com.phonecompany.service.interfaces.*;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.interfaces.Statistics;
import com.phonecompany.util.Query;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
        this.serviceDao = serviceDao;
        this.productCategoryDao = productCategoryDao;
        this.fileService = fileService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.statisticsService = statisticsService;
    }

    @Override
    @Cacheable
    public PagingResult<Service> getServicesByProductCategoryId(int page, int size, int productCategoryId,
                                                                String partOfName, double priceFrom, double priceTo,
                                                                int status, int orderBy, String orderByType) {
        if (priceFrom > 0 && priceTo > 0 && priceTo < priceFrom) {
            throw new ConflictException("Price from must be less than to");
        }
        Query query = this.buildQueryForTariffTable(page, size, productCategoryId,
                partOfName, priceFrom, priceTo, status, orderBy, orderByType);
        List<Service> services = this.serviceDao.executeForList(query.getQuery(),
                query.getPreparedStatementParams().toArray());
        List<Service> servicesWithDiscount = this.applyDiscount(services);
        LOG.debug("Services to be put in response: {}", servicesWithDiscount);

        int serviceEntityCount = this.serviceDao.executeForInt(query.getCountQuery(),
                query.getCountParams().toArray());

        PagingResult<Service> pagingResult = new PagingResult<>(servicesWithDiscount, serviceEntityCount);
        LOG.debug("Paging result to be returned: {}", pagingResult);
        return pagingResult;
    }

    @Cacheable
    @Override
    public List<Service> getAllActiveServicesWithDiscount() {
        return serviceDao.getAllActiveServicesWithDiscount();
    }

    @Cacheable
    @Override
    public List<Service> getTopActiveServices() {
        return serviceDao.getTopActiveServices();
    }

    @Cacheable
    @Override
    public List<Service> getServicesByStatus(ProductStatus status) {
        return serviceDao.getServicesByStatus(status);
    }

    private Query buildQueryForTariffTable(int page, int size, int productCategoryId,
                                           String partOfName, double priceFrom, double priceTo,
                                           int status, int orderBy, String orderByType) {
        Query.Builder builder = new Query.Builder("service " +
                "inner join product_category on service.prod_category_id = product_category.id");
        builder.where().addLikeCondition("service_name", partOfName);
        if (status == 1) builder.and().addCondition("product_status = ? ", "ACTIVATED");
        if (status == 2) builder.and().addCondition("product_status = ? ", "DEACTIVATED");
        if (productCategoryId > 0) builder.and().addCondition("prod_category_id = ?", productCategoryId);
        if (priceFrom > 0 && priceTo > 0) builder.and().addBetweenCondition("price", priceFrom, priceTo);
        if (priceFrom > 0) builder.and().addCondition("price >= ?", priceFrom);
        if (priceTo > 0) builder.and().addCondition("price <= ?", priceTo);
        String orderByField = buildOrderBy(orderBy);
        if (orderByField.length() > 0) {
            builder.orderBy(orderByField);
            builder.orderByType(orderByType);
        }
        builder.addPaging(page, size);
        return builder.build();
    }

    private String buildOrderBy(int orderBy) {
        switch (orderBy) {
            case 0://by name
                return "service_name";
            case 1://by price
                return "price";
            case 2://by status
                return "product_status";
            case 3://by category
                return "category_name";
            default:
                return "";
        }
    }

    /**
     * Applies a discount to all services depending on the current user.
     *
     * @param services services discount will be applied to
     * @return modified {@code List} of services
     */
    public List<Service> applyDiscount(List<Service> services) {
        Customer currentlyLoggedInUser = this.customerService.getCurrentlyLoggedInUser();
        LOG.debug("Currently logged in user: {}", currentlyLoggedInUser);
        if (currentlyLoggedInUser.getRepresentative()) {
            return this.mapToARepresentativePrice(services);
        }
        return services;
    }

    private List<Service> mapToARepresentativePrice(List<Service> services) {
        return services.stream()
                .map(TypeMapper.getDiscountMapper(REPRESENTATIVE_DISCOUNT))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable
    public Service getById(Long serviceId) {
        LOG.debug("Try to get service with id: {}", serviceId);
        Service serviceFoundById = super.getById(serviceId);
        if(serviceFoundById != null) {
            LOG.debug("Service price without a discount: {}", serviceFoundById.getPrice());
            Service serviceWithDiscount = this.applyDiscount(serviceFoundById);
            LOG.debug("Service price after applying discount: {}", serviceWithDiscount.getPrice());
            return serviceWithDiscount;
        }
        return null;
    }

    private Service applyDiscount(Service service) {
        Customer currentlyLoggedInUser = this.customerService.getCurrentlyLoggedInUser();
        LOG.debug("Currently logged in user: {}", currentlyLoggedInUser);
        return Optional.ofNullable(currentlyLoggedInUser)
                .map(this.getServiceIfLoggedInFunction(service))
                .orElse(service);
    }

    private Function<Customer, Service> getServiceIfLoggedInFunction(Service service) {
        return customer -> {
            Boolean isRepresentative = customer.getRepresentative();
            if (isRepresentative) {
                return TypeMapper.getDiscountMapper(REPRESENTATIVE_DISCOUNT).apply(service);
            }
            return service;
        };
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
            throw new ConflictException("Service with name " + service.getServiceName()
                    + " already exists");
        }
        String pictureUrl = this.getPictureUrlForService(service);
        service.setPictureUrl(pictureUrl);
        String productCategoryName = service.getProductCategory().getCategoryName();
        ProductCategory productCategory = productCategoryDao.getByName(productCategoryName);
        service.setProductCategory(productCategory);
        return super.save(service);
    }

    /**
     * Checks if such service already exists in the storage.
     *
     * @param service service to be verified
     * @return {@literal true} if service exists, {@literal false} otherwise
     */
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
    public SheetDataSet<LocalDate, Long> prepareStatisticsReportDataSet(LocalDate startDate, LocalDate endDate) {
        List<Statistics> statisticsList = this.orderService.getServiceOrderStatisticsByTimePeriod(startDate, endDate);
        if (statisticsList.size() == 0) {
            throw new MissingResultException("There were no tariff orders in this region during " +
                    "this period");
        }
        return statisticsService
                .prepareStatisticsDataSet("Services", statisticsList,
                        startDate, endDate);
    }

    @Override
    @Cacheable
    public Map<String, Object> getAllServicesSearch(int page, int size, String name, String status, int lowerPrice, int upperPrice) {
        Query.Builder queryBuilder = new Query.Builder("service");
        queryBuilder.where();
        queryBuilder.addLikeCondition("service_name", name);
        queryBuilder.and().addCondition("price > ?", lowerPrice);
        queryBuilder.and().addCondition("price < ?", upperPrice);
        if (status.equals("ACTIVATED") || status.equals("DEACTIVATED")) {
            queryBuilder.and().addCondition("product_status = ?", status);
        } else if (!status.equals("-")) {
            throw new ConflictException("Incorrect parameter: service status");
        }
        queryBuilder.addPaging(page, size);

        Map<String, Object> response = new HashMap<>();
        Query query = queryBuilder.build();
        response.put("services", serviceDao.executeForList(query.getQuery(), query.getPreparedStatementParams().toArray()));
        response.put("entitiesSelected", serviceDao.executeForInt(query.getCountQuery(), query.getCountParams().toArray()));
        return response;
    }

    @Cacheable
    @Override
    public List<Service> getAll() {
        return super.getAll();
    }
}
