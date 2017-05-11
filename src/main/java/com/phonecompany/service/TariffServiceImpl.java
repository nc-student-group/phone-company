package com.phonecompany.service;

import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.*;
import com.phonecompany.service.xssfHelper.ExcelSheet;
import com.phonecompany.service.xssfHelper.TariffFilteringStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@SuppressWarnings("Duplicates")
@Service
public class TariffServiceImpl extends CrudServiceImpl<Tariff>
        implements TariffService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffServiceImpl.class);

    private TariffDao tariffDao;
    private TariffRegionService tariffRegionService;
    private FileService fileService;
    private OrderService orderService;
    private CustomerTariffService customerTariffService;
    private XSSFService xssfService;

    @Autowired
    public TariffServiceImpl(TariffDao tariffDao,
                             TariffRegionService tariffRegionService,
                             FileService fileService,
                             OrderService orderService,
                             CustomerTariffService customerTariffService,
                             XSSFService xssfService) {
        super(tariffDao);
        this.tariffDao = tariffDao;
        this.tariffRegionService = tariffRegionService;
        this.fileService = fileService;
        this.orderService = orderService;
        this.customerTariffService = customerTariffService;
        this.xssfService = xssfService;
    }

    @Override
    public List<Tariff> getByRegionIdAndPaging(long regionId, int page, int size) {
        return this.tariffDao.getPaging(page, size, regionId);
    }

    @Override
    public Integer getCountByRegionId(long regionId) {
        return tariffDao.getEntityCount(regionId);
    }

    @Override
    public void updateTariffStatus(long tariffId, ProductStatus productStatus) {
        this.tariffDao.updateTariffStatus(tariffId, productStatus);
    }

    @Override
    public Tariff findByTariffName(String tariffName) {
        return this.tariffDao.findByTariffName(tariffName);
    }

    @Override
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
    public List<Tariff> getTariffsAvailableForCorporate() {
        return this.tariffDao.getTariffsAvailableForCorporate();
    }

    @Override
    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size) {
        return tariffDao.getTariffsAvailableForCustomer(regionId, page, size);
    }

    @Override
    public Integer getCountTariffsAvailableForCustomer(long regionId) {
        return tariffDao.getCountTariffsAvailableForCustomer(regionId);
    }

    @Override
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
    public Map<String, Object> getTariffsTable(long regionId, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<Tariff> tariffs = this.getByRegionIdAndPaging(regionId, page, size);
        List<Object> rows = new ArrayList<>();
        tariffs.forEach((Tariff tariff) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("tariff", tariff);
            row.put("regions", tariffRegionService.getAllByTariffId(tariff.getId()));
            rows.add(row);
        });
        response.put("tariffs", rows);
        response.put("tariffsSelected", this.getCountByRegionId(regionId));
        return response;
    }

    @Override
    public ExcelSheet generateTariffReport(long regionId, LocalDate startDate, LocalDate endDate) {

        List<Order> tariffOrders = this.orderService
                .getTariffOrdersByRegionIdAndTimePeriod(regionId, startDate, endDate);

        TariffFilteringStrategy tariffFilteringStrategy = new TariffFilteringStrategy();

        Map<String, List<Order>> productNamesToOrdersMap = this.orderService
                .getProductNamesToOrdersMap(tariffOrders, tariffFilteringStrategy);

        List<LocalDate> timeLine = this.orderService.generateTimeLine(tariffOrders);

        return this.orderService
                .prepareExcelSheetDataset("Tariffs", productNamesToOrdersMap, timeLine);
    }
}
