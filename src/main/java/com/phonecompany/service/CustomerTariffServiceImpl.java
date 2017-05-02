package com.phonecompany.service;

import com.phonecompany.dao.OrderDaoImpl;
import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.service.interfaces.CustomerTariffService;
import com.phonecompany.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerTariffServiceImpl extends CrudServiceImpl<CustomerTariff> implements CustomerTariffService {

    private CustomerTariffDao customerTariffDao;
    private OrderService orderService;

    @Autowired
    public CustomerTariffServiceImpl(CustomerTariffDao customerTariffDao, OrderService orderService){
        super(customerTariffDao);
        this.customerTariffDao = customerTariffDao;
        this.orderService = orderService;
    }

    @Override
    public List<CustomerTariff> getByClientId(Customer customer) {
        return customer.getRepresentative() ?
                customerTariffDao.getCustomerTariffsByCorporateId(customer.getCorporate().getId()) :
                customerTariffDao.getCustomerTariffsByCustomerId(customer.getId());
    }

    @Override
    public CustomerTariff getCurrentCustomerTariff(long customerId){
        return this.customerTariffDao.getCurrentCustomerTariff(customerId);
    }

    @Override
    public CustomerTariff getCurrentCorporateTariff(long corporateId){
        return this.customerTariffDao.getCurrentCorporateTariff(corporateId);
    }

    @Override
    public CustomerTariff getCurrentActiveOrSuspendedClientTariff(Customer customer) {
        return customer.getRepresentative() ?
                customerTariffDao.getCurrentActiveOrSuspendedCorporateTariff(customer.getCorporate().getId()) :
                customerTariffDao.getCurrentActiveOrSuspendedCustomerTariff(customer.getId());
    }

    @Override
    public CustomerTariff deactivateCustomerTariff(CustomerTariff customerTariff) {
        customerTariff.setCustomerProductStatus(CustomerProductStatus.DEACTIVATED);
        customerTariffDao.update(customerTariff);
        Order order = new Order();
        order.setCustomerTariff(customerTariff);
//        order.setCreationDate(LocalDate.now());
        return null;
    }
}
