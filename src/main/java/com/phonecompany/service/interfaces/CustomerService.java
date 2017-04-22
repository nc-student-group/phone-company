package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.OnRegistrationCompleteEvent;

public interface CustomerService extends CrudService<Customer>, AbstractUserService<Customer> {
    void activateUserByToken(String token);
    void confirmRegistration(OnRegistrationCompleteEvent<Customer> registrationCompleteEvent);
}
