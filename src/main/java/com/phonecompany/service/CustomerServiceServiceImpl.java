package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.model.CustomerService;
import com.phonecompany.service.interfaces.CustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceServiceImpl extends CrudServiceImpl<CustomerService> implements CustomerServiceService{

    private CustomerServiceDao customerServiceDao;

    @Autowired
    public CustomerServiceServiceImpl(CustomerServiceDao customerServiceDao){
        super(customerServiceDao);
        this.customerServiceDao = customerServiceDao;
    }

}
