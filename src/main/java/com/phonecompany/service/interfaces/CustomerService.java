package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.events.OnRegistrationCompleteEvent;

import java.util.List;

public interface CustomerService extends CrudService<Customer>,
        AbstractUserService<Customer> {
    void activateUserByToken(String token);

    void confirmRegistration(OnRegistrationCompleteEvent registrationCompleteEvent);

    List<Customer> getAllCustomersPaging(int page, int size, long rId, String status);

    int getCountCustomers(long rId, String status);

    void deactivateCustomer(long id);

    List<Customer> getCustomersByCorporate(long corporateId);
}
