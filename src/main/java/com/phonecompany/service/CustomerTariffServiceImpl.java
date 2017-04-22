package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerTariffDao;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.service.interfaces.CustomerTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerTariffServiceImpl extends CrudServiceImpl<CustomerTariff> implements CustomerTariffService {

    private CustomerTariffDao customerTariffDao;

    @Autowired
    public CustomerTariffServiceImpl(CustomerTariffDao customerTariffDao){
        super(customerTariffDao);
        this.customerTariffDao = customerTariffDao;
    }
}
