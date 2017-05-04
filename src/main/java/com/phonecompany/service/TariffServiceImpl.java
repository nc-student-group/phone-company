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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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
    private MailMessageCreator<Tariff> tariffActivationNotificationEmailCreator;
    private MailMessageCreator<Tariff> tariffNotificationEmailCreator;
    private EmailService<Customer> emailService;
    private CustomerService customerService;

    @Autowired
    public TariffServiceImpl(TariffDao tariffDao,
                             TariffRegionService tariffRegionService,
                             FileService fileService,
                             OrderService orderService,
                             CustomerTariffService customerTariffService,
                             @Qualifier("tariffActivationNotificationEmailCreator")
                             MailMessageCreator<Tariff> tariffActivationNotificationEmailCreator,
                             @Qualifier("tariffNotificationEmailCreator")
                             MailMessageCreator<Tariff> tariffNotificationEmailCreator,
                             EmailService<Customer> emailService,
                             CustomerService customerService) {
        super(tariffDao);
        this.tariffDao = tariffDao;
        this.tariffRegionService = tariffRegionService;
        this.fileService = fileService;
        this.orderService = orderService;
        this.customerTariffService = customerTariffService;
        this.tariffActivationNotificationEmailCreator = tariffActivationNotificationEmailCreator;
        this.emailService = emailService;
        this.tariffNotificationEmailCreator = tariffNotificationEmailCreator;
        this.customerService = customerService;
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
                return new ResponseEntity<>(new Error("Tariff with name \""
                        + tariff.getTariffName() + "\" already exist!"), HttpStatus.BAD_REQUEST);
            }
            tariff.setPictureUrl(fileService.stringToFile(tariff.getPictureUrl(), "tariff/"
                    + tariff.hashCode()));
            Tariff savedTariff = this.update(tariff);
            LOGGER.debug("Tariff added {}", tariff);
            tariffRegionService.deleteByTariffId(savedTariff.getId());
            this.addTariffRegions(tariffRegions, savedTariff);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
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
        LOGGER.debug("TARIFF PRICE: " + tariffRegion.getPrice() * (1 - tariffRegion.getTariff().getDiscount() / 100));
        CustomerTariff customerTariff = new CustomerTariff(customer, null, tariffRegion.getPrice() * (1 - tariffRegion.getTariff().getDiscount()/100), CustomerProductStatus.ACTIVE, tariffRegion.getTariff());
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
        LOGGER.debug("TARIFF PRICE: " + tariff.getPrice() * (1 - tariff.getDiscount()/100));
        CustomerTariff customerTariff = new CustomerTariff(null, corporate, tariff.getPrice() * (1 - tariff.getDiscount()/100), CustomerProductStatus.ACTIVE, tariff);
        customerTariffService.save(customerTariff);
        activationOrder.setCustomerTariff(customerTariff);
        activationOrder.setOrderStatus(OrderStatus.DONE);
        orderService.update(activationOrder);
        LOGGER.debug("Tariff " + customerTariff.getId() + " activated.");
    }

    @Override
    public ResponseEntity<?> activateTariff(long tariffId, Customer customer) {
        if (customer.getCorporate() == null) {
            return this.activateTariffForSingleCustomer(tariffId, customer);
        } else {
            if (customer.getRepresentative()) {
                return this.activateTariffForCorporateCustomer(tariffId, customer);
            } else {
                return new ResponseEntity<Object>(new Error("You aren't representative of your company. Contact with your company representative to change tariff plan."), HttpStatus.CONFLICT);
            }
        }

    }

    private ResponseEntity<?> activateTariffForSingleCustomer(long tariffId, Customer customer) {
        TariffRegion tariffRegion = tariffRegionService.getByTariffIdAndRegionId(tariffId, customer.getAddress().getRegion().getId());
        if (tariffRegion == null) {
            return new ResponseEntity<Object>(new Error("This tariff plan for your region doesn't exist. Choose tariff plan form available list."), HttpStatus.CONFLICT);
        }
        if (!tariffRegion.getTariff().getProductStatus().equals(ProductStatus.ACTIVATED)) {
            return new ResponseEntity<Object>(new Error("This tariff plan is deactivated at the moment."), HttpStatus.CONFLICT);
        }
        CustomerTariff customerTariff = customerTariffService.getCurrentCustomerTariff(customer.getId());
        if (customerTariff != null) {
            this.deactivateSingleTariff(customerTariff);
        }
        this.activateSingleTariff(customer, tariffRegion);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    private ResponseEntity<?> activateTariffForCorporateCustomer(long tariffId, Customer customer) {
        Tariff tariff = this.getById(tariffId);
        if (tariff == null) {
            return new ResponseEntity<Object>(new Error("This tariff plan doesn't exist. Choose tariff plan form available list."), HttpStatus.CONFLICT);
        }
        if (!tariff.getProductStatus().equals(ProductStatus.ACTIVATED)) {
            return new ResponseEntity<Object>(new Error("This tariff plan is deactivated at the moment."), HttpStatus.CONFLICT);
        }
        CustomerTariff customerTariff = customerTariffService.getCurrentCorporateTariff(customer.getCorporate().getId());
        if (customerTariff != null) {
            this.deactivateCorporateTariff(customerTariff);
        }
        this.activateCorporateTariff(customer.getCorporate(), tariff);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addNewTariff(Tariff tariff, List<TariffRegion> tariffRegions){
        LocalDate currentDate = LocalDate.now();
        if (this.findByTariffName(tariff.getTariffName()) != null) {
            return new ResponseEntity<>(new Error("Tariff with name \"" +
                    tariff.getTariffName() + "\" already exist!"), HttpStatus.BAD_REQUEST);
        }
        tariff.setProductStatus(ProductStatus.ACTIVATED);
        tariff.setCreationDate(currentDate);
        tariff.setPictureUrl(fileService.stringToFile(tariff.getPictureUrl(),
                "tariff/" + tariff.hashCode())); // no such thing as get time in millis
        Tariff savedTariff = this.save(tariff);       // in LocalDate class -> changed to hashcode

        //???????????????
        Customer currentlyLoggedInUser = this.customerService.getCurrentlyLoggedInUser();
        SimpleMailMessage notificationMessage =
                this.tariffNotificationEmailCreator.constructMessage(savedTariff);
        this.emailService.sendMail(notificationMessage, currentlyLoggedInUser);
        //???????????????

        LOGGER.debug("Tariff added {}", savedTariff);
        this.addTariffRegions(tariffRegions, savedTariff);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
