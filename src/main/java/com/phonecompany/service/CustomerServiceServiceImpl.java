package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.Order;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.interfaces.CustomerService;
import com.phonecompany.service.interfaces.CustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;

@Service
public class CustomerServiceServiceImpl extends CrudServiceImpl<CustomerServiceDto> implements CustomerServiceService{

    private CustomerServiceDao customerServiceDao;

    @Autowired
    public CustomerServiceServiceImpl(CustomerServiceDao customerServiceDao){
        super(customerServiceDao);
        this.customerServiceDao = customerServiceDao;
    }

    @Override
    public List<CustomerServiceDto> getCurrentCustomerServices(long customerId) {
        return customerServiceDao.getCurrentCustomerServices(customerId);
    }

    @Override
    public List<CustomerServiceDto> getCustomerServicesByCustomerId(long customerId) {
        return customerServiceDao.getCustomerServicesByCustomerId(customerId);
    }
}
