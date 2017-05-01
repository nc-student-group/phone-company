package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.service.interfaces.CustomerTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerTariffServiceImpl extends CrudServiceImpl<CustomerTariff> implements CustomerTariffService {

    private CustomerTariffDao customerTariffDao;

    @Autowired
    public CustomerTariffServiceImpl(CustomerTariffDao customerTariffDao){
        super(customerTariffDao);
        this.customerTariffDao = customerTariffDao;
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
}
