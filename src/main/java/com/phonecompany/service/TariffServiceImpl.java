package com.phonecompany.service;

import com.phonecompany.annotations.CacheClear;
import com.phonecompany.annotations.Cacheable;
import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.exception.EmptyResultSetException;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.*;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@ServiceStereotype
public class TariffServiceImpl extends CrudServiceImpl<Tariff>
        implements TariffService, ExtendedStatisticsGenerating<LocalDate, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffServiceImpl.class);

    private TariffDao tariffDao;
    private TariffRegionService tariffRegionService;
    private FileService fileService;
    private OrderService orderService;
    private CustomerTariffService customerTariffService;
    private StatisticsService<LocalDate, Long> statisticsService;

    @Autowired
    public TariffServiceImpl(TariffDao tariffDao,
                             TariffRegionService tariffRegionService,
                             FileService fileService,
                             OrderService orderService,
                             CustomerTariffService customerTariffService,
                             StatisticsService<LocalDate, Long> statisticsService) {
        super(tariffDao);
        this.tariffDao = tariffDao;
        this.tariffRegionService = tariffRegionService;
        this.fileService = fileService;
        this.orderService = orderService;
        this.customerTariffService = customerTariffService;
        this.statisticsService = statisticsService;
    }

    @Override
    @CacheClear
    public void updateTariffStatus(long tariffId, ProductStatus productStatus) {
        this.tariffDao.updateTariffStatus(tariffId, productStatus);
    }

    @Override
    public Tariff findByTariffName(String tariffName) {
        return this.tariffDao.findByTariffName(tariffName);
    }

    @Override
    @CacheClear
    public Tariff updateTariff(List<TariffRegion> tariffRegions) {
        if (tariffRegions.size() > 0) {
            Tariff savedTariff = updateTariff(tariffRegions.get(0).getTariff());
            tariffRegionService.deleteByTariffId(savedTariff.getId());
            this.addTariffRegions(tariffRegions, savedTariff);
            return savedTariff;
        } else {
            throw new ConflictException("Tariff must contain regions with prices!");
        }
    }

    @Override
    @CacheClear
    public Tariff updateTariff(Tariff tariff) {
        Tariff temp = this.findByTariffName(tariff.getTariffName());
        if (temp != null && !temp.getId().equals(tariff.getId())) {
            throw new ConflictException("Tariff with name \""
                    + tariff.getTariffName() + "\" already exist!");
        }
        tariff.setPictureUrl(fileService.stringToFile(tariff.getPictureUrl(), "tariff/"
                + tariff.hashCode()));
        Tariff savedTariff = this.update(tariff);
        LOGGER.debug("Tariff updated {}", tariff);
        return savedTariff;
    }

    private void addTariffRegions(List<TariffRegion> tariffRegions, Tariff tariff) {
        tariffRegions.forEach((TariffRegion tariffRegion) -> {
            if (tariffRegion.getPrice() > 0 && tariffRegion.getRegion() != null) {
                tariffRegion.setTariff(tariff);
                tariffRegionService.save(tariffRegion);
                LOGGER.debug("Tariff-region added {}", tariffRegion);
            }
        });
    }

    public Map<String, Object> getTariffsAvailableForCustomer(Customer customer, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        if (customer.getCorporate() == null) {
            response.put("tariffs",
                    this.getTariffsAvailableForCustomer(customer.getAddress().getRegion().getId(), page, size));
            response.put("tariffsCount",
                    this.getCountTariffsAvailableForCustomer(customer.getAddress().getRegion().getId()));
        } else {
            if (customer.getRepresentative()) {
                response.put("tariffs", this.getTariffsAvailableForCorporate(page, size));
                response.put("tariffsCount", this.getCountTariffsAvailableForCorporate());
            } else {
                throw new ConflictException("You aren't representative of your company.");
            }
        }
        return response;
    }

    @Override
    @Cacheable
    public List<Tariff> getTariffsAvailableForCustomer(Customer customer) {
        if (customer.getCorporate() == null) {
            return tariffDao.getTariffsAvailableForCustomer(customer.getAddress().getRegion().getId());
        } else {
            if (customer.getRepresentative()) {
                return tariffDao.getTariffsAvailableForCorporate();
            } else {
                throw new ConflictException("You aren't representative of your company.");
            }
        }
    }

    @Override
    @Cacheable
    public List<Tariff> getTariffsAvailableForCorporate() {
        return this.tariffDao.getTariffsAvailableForCorporate();
    }

    @Override
    @Cacheable
    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size) {
        LOGGER.debug("getTariffsAvailableForCustomer");
        return tariffDao.getTariffsAvailableForCustomer(regionId, page, size);
    }

    @Override
    public Integer getCountTariffsAvailableForCustomer(long regionId) {
        return tariffDao.getCountTariffsAvailableForCustomer(regionId);
    }

    @Override
    @Cacheable
    public List<Tariff> getTariffsAvailableForCorporate(int page, int size) {
        return this.tariffDao.getTariffsAvailableForCorporate(page, size);
    }

    @Override
    public Integer getCountTariffsAvailableForCorporate() {
        return this.tariffDao.getCountTariffsAvailableForCorporate();
    }

    @Override
    public Tariff getByIdForSingleCustomer(long id, long regionId) {
        return this.tariffDao.getByIdForSingleCustomer(id, regionId);
    }

    @Override
    public void deactivateSingleTariff(CustomerTariff customerTariff) {
        LOGGER.debug("Tariff deactivation for customer id " + customerTariff.getCustomer().getId());
        LocalDate currentDate = LocalDate.now();
        Order deactivationOrder = new Order(null, customerTariff,
                OrderType.DEACTIVATION, OrderStatus.CREATED, currentDate, currentDate);
        orderService.save(deactivationOrder);
        customerTariff.setCustomerProductStatus(CustomerProductStatus.DEACTIVATED);
        customerTariffService.update(customerTariff);
        deactivationOrder.setOrderStatus(OrderStatus.DONE);
        orderService.update(deactivationOrder);
        LOGGER.debug("Tariff " + customerTariff.getId() + " deactivated.");
    }

    @Override
    public void activateSingleTariff(Customer customer, TariffRegion tariffRegion) {
        LOGGER.debug("Tariff deactivation for customer id " + customer.getId());
        LocalDate currentDate = LocalDate.now();
        Order activationOrder = new Order(null, null,
                OrderType.ACTIVATION, OrderStatus.CREATED, currentDate, currentDate);
        orderService.save(activationOrder);
        LOGGER.debug("TARIFF PRICE: "
                + tariffRegion.getPrice() * (1 - tariffRegion.getTariff().getDiscount() / 100));
        CustomerTariff customerTariff = new CustomerTariff(customer, null,
                tariffRegion.getPrice() * (1 - tariffRegion.getTariff().getDiscount() / 100),
                CustomerProductStatus.ACTIVE, tariffRegion.getTariff());
        customerTariffService.save(customerTariff);
        activationOrder.setCustomerTariff(customerTariff);
        activationOrder.setOrderStatus(OrderStatus.DONE);
        orderService.update(activationOrder);
        LOGGER.debug("Tariff " + customerTariff.getId() + " activated.");
    }

    @Override
    public void deactivateCorporateTariff(CustomerTariff customerTariff) {
        LOGGER.debug("Tariff deactivation for corporate id " + customerTariff.getCorporate().getId());
        LocalDate currentDate = LocalDate.now();
        Order deactivationOrder = new Order(null, customerTariff,
                OrderType.DEACTIVATION, OrderStatus.CREATED, currentDate, currentDate);
        orderService.save(deactivationOrder);
        customerTariff.setCustomerProductStatus(CustomerProductStatus.DEACTIVATED);
        customerTariffService.update(customerTariff);
        deactivationOrder.setOrderStatus(OrderStatus.DONE);
        orderService.update(deactivationOrder);
        LOGGER.debug("Tariff " + customerTariff.getId() + " deactivated.");
    }

    @Override
    public void activateCorporateTariff(Corporate corporate, Tariff tariff) {
        LOGGER.debug("Tariff deactivation for corporate id " + corporate.getId());
        LocalDate currentDate = LocalDate.now();
        Order activationOrder = new Order(null, null,
                OrderType.ACTIVATION, OrderStatus.CREATED, currentDate, currentDate);
        orderService.save(activationOrder);
        LOGGER.debug("TARIFF PRICE: " + tariff.getPrice() * (1 - tariff.getDiscount() / 100));
        CustomerTariff customerTariff = new CustomerTariff(null, corporate,
                tariff.getPrice() * (1 - tariff.getDiscount() / 100), CustomerProductStatus.ACTIVE, tariff);
        customerTariffService.save(customerTariff);
        activationOrder.setCustomerTariff(customerTariff);
        activationOrder.setOrderStatus(OrderStatus.DONE);
        orderService.update(activationOrder);
        LOGGER.debug("Tariff " + customerTariff.getId() + " activated.");
    }

    @Override
    public void activateTariff(long tariffId, Customer customer) {
        if (customer.getCorporate() == null) {
            this.activateTariffForSingleCustomer(tariffId, customer);
        } else {
            if (customer.getRepresentative()) {
                activateTariffForCorporateCustomer(tariffId, customer.getCorporate());
            } else {
                throw new ConflictException("You aren't representative of your company." +
                        " Contact with your company representative to change tariff plan.");
            }
        }

    }

    @Override
    public void activateTariffForSingleCustomer(long tariffId, Customer customer) {
        TariffRegion tariffRegion = tariffRegionService
                .getByTariffIdAndRegionId(tariffId, customer.getAddress().getRegion().getId());
        if (tariffRegion == null) {
            throw new ConflictException("This tariff plan for your region doesn't exist." +
                    " Choose tariff plan form available list.");
        }
        if (!tariffRegion.getTariff().getProductStatus().equals(ProductStatus.ACTIVATED)) {
            throw new ConflictException("This tariff plan is deactivated at the moment.");
        }
        CustomerTariff customerTariff = customerTariffService.getCurrentCustomerTariff(customer.getId());
        if (customerTariff != null) {
            this.deactivateSingleTariff(customerTariff);
        }
        this.activateSingleTariff(customer, tariffRegion);
    }

    @Override
    public void activateTariffForCorporateCustomer(long tariffId, Corporate corporate) {
        Tariff tariff = this.getById(tariffId);
        if (tariff == null) {
            throw new ConflictException("This tariff plan doesn't exist. Choose tariff plan form available list.");
        }
        if (!tariff.getProductStatus().equals(ProductStatus.ACTIVATED)) {
            throw new ConflictException("This tariff plan is deactivated at the moment.");
        }
        CustomerTariff customerTariff = customerTariffService.getCurrentCorporateTariff(corporate.getId());
        if (customerTariff != null) {
            this.deactivateCorporateTariff(customerTariff);
        }
        this.activateCorporateTariff(corporate, tariff);
    }

    @Override
    @CacheClear
    public Tariff addNewTariff(List<TariffRegion> tariffRegions) {
        Tariff tariff;
        if (tariffRegions.size() > 0) {
            tariff = tariffRegions.get(0).getTariff();
        } else {
            throw new ConflictException("Tariff must contain regions with prices!");
        }
        Tariff savedTariff = this.addNewTariff(tariff);

        LOGGER.debug("Tariff added {}", savedTariff);
        this.addTariffRegions(tariffRegions, savedTariff);
        return savedTariff;
    }

    @Override
    @CacheClear
    @Transactional
    public Tariff addNewTariff(Tariff tariff) {
        if (this.findByTariffName(tariff.getTariffName()) != null) {
            throw new ConflictException("Tariff with name \"" +
                    tariff.getTariffName() + "\" already exist!");
        }
        tariff.setProductStatus(ProductStatus.ACTIVATED);
        tariff.setCreationDate(LocalDate.now());
        tariff.setPictureUrl(fileService.stringToFile(tariff.getPictureUrl(),
                "tariff/" + tariff.hashCode()));
        Tariff savedTariff = this.save(tariff);
        LOGGER.debug("Tariff added {}", savedTariff);
        return savedTariff;
    }

    @Override
    public Tariff getTariffForCustomer(long tariffId, Customer customer) {
        if (customer.getCorporate() == null) {
            return this.getByIdForSingleCustomer(tariffId, customer.getAddress().getRegion().getId());
        } else {
            return this.getById(tariffId);
        }
    }

    @Override
    @Cacheable
    public Map<String, Object> getTariffsTable(int page, int size, String name, int status,
                                               int type, String fromStr, String toStr, int orderBy, String orderByType) {
        java.sql.Date from = null, to = null;
        if (!fromStr.equals("null")) {
            from = java.sql.Date.valueOf(fromStr);
        }
        if (!toStr.equals("null")) {
            to = java.sql.Date.valueOf(toStr);
        }
        if (from != null && to != null && from.getTime() > to.getTime()) {
            throw new ConflictException("Date from must be less then to");
        }
        Query query = this.buildQueryForTariffTable(page, size, name, status,
                type, from, to, orderBy, orderByType);
        Map<String, Object> response = new HashMap<>();
        response.put("tariffs", this.tariffDao.executeForList(query.getQuery(), query.getPreparedStatementParams().toArray()));
        response.put("tariffsSelected", this.tariffDao.executeForInt(query.getCountQuery(),
                query.getCountParams().toArray()));
        return response;
    }

    private Query buildQueryForTariffTable(int page, int size, String name, int status,
                                           int type, Date from, Date to, int orderBy, String orderByType) {
        Query.Builder builder = new Query.Builder("tariff");
        builder.where().addLikeCondition("tariff_name", name);
        if (status == 1) builder.and().addCondition("product_status = ? ", "DEACTIVATED");
        if (status == 2) builder.and().addCondition("product_status = ? ", "ACTIVATED");
        if (type == 1) builder.and().addCondition("is_corporate = ? ", true);
        if (type == 2) builder.and().addCondition("is_corporate = ? ", false);
        if (from != null && to != null) builder.and().addBetweenCondition("creation_date", from, to);
        if (from != null) builder.and().addCondition("creation_date >= ?", from);
        if (to != null) builder.and().addCondition("creation_date <= ?", to);
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
            case 0://by date
                return "creation_date";
            case 1://by name
                return "tariff_name";
            case 2://by status
                return "product_status";
            case 3://by type
                return "tariff_name";
            default:
                return "";
        }
    }

    /**
     * Responsible for creating a dataset containing statistical information regarding
     * the tariff orders made in the requested region in some predefined period of time
     *
     * @param regionId  id of the region statistics should be generated for
     * @param startDate start of the period statistics should be generated for
     * @param endDate   end of the period statistics should be generated for
     * @return fully constructed {@link SheetDataSet}
     */
    @Override
    public SheetDataSet<LocalDate, Long> getTariffStatisticsDataSet(long regionId,
                                                                    LocalDate startDate,
                                                                    LocalDate endDate) {
        List<Statistics> statisticsList = this.orderService
                .getTariffOrderStatisticsByRegionAndTimePeriod(regionId, startDate, endDate);
        if (statisticsList.size() == 0) {
            throw new EmptyResultSetException("There were no tariff orders in this region during " +
                    "this period");
        }
        return statisticsService
                .prepareStatisticsDataSet("Tariffs", statisticsList,
                        startDate, endDate);
    }

    @Override
    public List<Tariff> getAllTariffsSearch(int page, int size, String name, String status, String category) {

        Query.Builder query = new Query.Builder("tariff");
        query.where().addLikeCondition("tariff_name", name);
        if (!status.equals("-") && (status.equals("ACTIVATED") || status.equals("DEACTIVATED"))) {
            query.and().addCondition("product_status=?", status);
        } else if (!status.equals("-")) {
            throw new ConflictException("Incorrect parameter: tariff status.");
        }

        if (category.equals("COMPANY")) {
            query.and().addCondition("is_corporate=?", true);
        } else if (category.equals("PRIVATE")) {
            query.and().addCondition("is_corporate=?", false);
        } else if (!category.equals("-")) {
            throw new ConflictException("Incorrect parameter: is corporate tariff");
        }
        query.addPaging(page, size);
        return tariffDao.getAllTariffsSearch(query.build());
    }

    @Override
    public int getCountSearch(int page, int size, String name, String status, String category) {
        Query.Builder query = new Query.Builder("tariff");
        query.where().addLikeCondition("tariff_name", name);
        if (!status.equals("-") && (status.equals("ACTIVATED") || status.equals("DEACTIVATED"))) {
            query.and().addCondition("product_status=?", status);
        } else if (!status.equals("-")) {
            throw new ConflictException("Incorrect parameter: tariff status.");
        }

        if (category.equals("COMPANY")) {
            query.and().addCondition("is_corporate=?", true);
        } else if (category.equals("PRIVATE")) {
            query.and().addCondition("is_corporate=?", false);
        } else if (!category.equals("-")) {
            throw new ConflictException("Incorrect parameter: is corporate tariff");
        }
        return tariffDao.getAllTariffsSearch(query.build()).size();
    }

}
