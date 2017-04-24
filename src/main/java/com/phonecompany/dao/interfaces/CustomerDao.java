package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Customer;

import java.util.List;

public interface CustomerDao extends CrudDao<Customer>, AbstractUserDao<Customer>  {
    Customer getByVerificationToken(String token);

    public List<Customer> getAllCustomersPaging(int page, int size,long rId,String status);
    public int getCountCustomers(long rId,String status);
}
