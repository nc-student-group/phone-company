package com.phonecompany.service;

import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TariffServiceImpl extends CrudServiceImpl<Tariff> implements TariffService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffServiceImpl.class);

    private TariffDao tariffDao;
    private TariffRegionService tariffRegionService;
    private FileService fileService;
    private OrderService orderService;
    private CustomerTariffService customerTariffService;

    @Autowired
    public TariffServiceImpl(TariffDao tariffDao, TariffRegionService tariffRegionService, FileService fileService, OrderService orderService, CustomerTariffService customerTariffService) {
        super(tariffDao);
        this.tariffDao = tariffDao;
        this.tariffRegionService = tariffRegionService;
        this.fileService = fileService;
        this.orderService = orderService;
        this.customerTariffService = customerTariffService;
    }

    @Override
    public List<Tariff> getByRegionIdAndPaging(long regionId, int page, int size) {
        return tariffDao.getByRegionIdAndPaging(regionId, page, size);
    }

    @Override
    public List<Tariff> getByRegionIdAndClient(Long regionId, Boolean isRepresentative) {
        return tariffDao.getByRegionId(regionId).stream()
                .filter(t -> (t.getProductStatus().equals(ProductStatus.ACTIVATED) &&
                        isRepresentative.equals(t.isCorporate())))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getCountByRegionIdAndPaging(long regionId) {
        return tariffDao.getCountByRegionIdAndPaging(regionId);
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
        response.put("tariffsSelected", this.getCountByRegionIdAndPaging(regionId));
        return response;
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
    public ResponseEntity<?> updateTariffAndRegions(List<TariffRegion> tariffRegions) {
        if (tariffRegions.size() > 0) {
            Tariff tariff = tariffRegions.get(0).getTariff();
            Tariff temp = this.findByTariffName(tariff.getTariffName());
            if (temp != null && temp.getId() != tariff.getId()) {
                return new ResponseEntity<>(new Error("Tariff with name \"" + tariff.getTariffName() + "\" already exist!"), HttpStatus.BAD_REQUEST);
            }
            tariff.setPictureUrl(fileService.stringToFile(tariff.getPictureUrl(), "tariff/" + tariff.getCreationDate().getTime()));
            Tariff savedTariff = this.update(tariff);
            LOGGER.debug("Tariff added {}", tariff);
            tariffRegionService.deleteByTariffId(savedTariff.getId());
            tariffRegions.forEach((TariffRegion tariffRegion) -> {
                if (tariffRegion.getPrice() > 0 && tariffRegion.getRegion() != null) {
                    tariffRegion.setTariff(savedTariff);
                    tariffRegionService.save(tariffRegion);
                    LOGGER.debug("Tariff-region added {}", tariffRegion);
                }
            });
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
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
    public void deactivateTariff(CustomerTariff customerTariff) {
        LOGGER.debug("Tariff deactivation for customer id "+ customerTariff.getCustomer().getId());
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        Order deactivationOrder = new Order(null, customerTariff, OrderType.DEACTIVATION, OrderStatus.CREATED, date, date);
        orderService.save(deactivationOrder);
        customerTariff.setCustomerProductStatus(CustomerProductStatus.DEACTIVATED);
        customerTariffService.update(customerTariff);
        deactivationOrder.setOrderStatus(OrderStatus.DONE);
        orderService.update(deactivationOrder);
        LOGGER.debug("Tariff "+ customerTariff.getId() + " deactivated.");
    }

    @Override
    public void activateTariff(Customer customer, TariffRegion tariffRegion){
        LOGGER.debug("Tariff deactivation for customer id "+ customer.getId());
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        Order activationOrder = new Order(null, null, OrderType.ACTIVATION, OrderStatus.CREATED, date, date);
        orderService.save(activationOrder);
        LOGGER.debug("TARIFF PRICE: "+tariffRegion.getPrice() * (1 - tariffRegion.getTariff().getDiscount()));
        CustomerTariff customerTariff = new CustomerTariff(customer, null, tariffRegion.getPrice() * (1 - tariffRegion.getTariff().getPrice()), CustomerProductStatus.ACTIVE, tariffRegion.getTariff());
        customerTariffService.save(customerTariff);
        activationOrder.setCustomerTariff(customerTariff);
        activationOrder.setOrderStatus(OrderStatus.DONE);
        orderService.update(activationOrder);
        LOGGER.debug("Tariff "+ customerTariff.getId() + " activated.");
    }

}
