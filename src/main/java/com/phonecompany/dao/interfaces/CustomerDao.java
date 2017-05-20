package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.util.Query;

import java.util.List;

public interface CustomerDao extends JdbcOperations<Customer>,
        AbstractUserDao<Customer>, AbstractPageableDao<Customer> {

    Customer getByVerificationToken(String token);

    List<Customer> getByCorporateId(long corporateId);

    int getCountByPhone(String phone);

    int getCountByEmail(String email);

    List<Customer> getAllCustomersSearch(Query query);
}
