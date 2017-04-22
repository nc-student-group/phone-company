package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Customer;

public interface CustomerDao extends CrudDao<Customer>, AbstractUserDao<Customer>  {
    Customer getUserByVerificationToken(String token);
}
