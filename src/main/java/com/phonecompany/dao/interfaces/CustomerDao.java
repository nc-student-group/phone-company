package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.util.Query;

import java.util.List;

public interface CustomerDao extends JdbcOperations<Customer>,
        AbstractUserDao<Customer> {

    Customer getByVerificationToken(String token);

    List<Customer> getByCorporateId(long corporateId);

    List<Customer> getSuitableCustomersForService(long corporateId);

    int getCountByPhone(String phone);

    int getCountByEmail(String email);

    List<Customer> getCustomersAgreedForMailing();

    List<Customer> getAllCustomersSearch(Query query);

    void changeMailingAgreement(boolean state, long customerId);
}
