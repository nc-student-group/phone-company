package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerServiceDto;
import com.phonecompany.model.Service;
import com.phonecompany.model.events.OnRegistrationCompleteEvent;

import java.util.List;

public interface CustomerService extends CrudService<Customer>,
        AbstractUserService<Customer> {
    void activateUserByToken(String token);

    void confirmRegistration(OnRegistrationCompleteEvent registrationCompleteEvent);

    List<Customer> getAllCustomersPaging(int page, int size, long rId, String status);

    int getCountCustomers(long rId, String status);

    void deactivateCustomer(long id);

    void deactivateCustomerTariff(long id);

    List<Customer> getCustomersByCorporate(long corporateId);

    List<Customer> getAllCustomersSearch(String email,String phone, String surname,int corporate, int region,String status);
}
