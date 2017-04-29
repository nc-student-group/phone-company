package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao extends CrudDao<Customer>,
        AbstractUserDao<Customer>, AbstractPageableDao<Customer> {

    Customer getByVerificationToken(String token);

    List<Customer> getPagingByParametersMap(int page, int size, Map<String, Object> params);
}
