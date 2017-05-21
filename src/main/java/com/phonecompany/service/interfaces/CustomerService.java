package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.events.OnRegistrationCompleteEvent;
import java.util.List;
import java.util.Map;

public interface CustomerService extends CrudService<Customer>,
        AbstractUserService<Customer> {
    void activateUserByToken(String token);

    void confirmRegistration(OnRegistrationCompleteEvent registrationCompleteEvent);

    Map<String, Object> getAllCustomersPaging(int page, int size, long regionId, String status, String partOfEmail,
                                              String partOfName, String selectedPhone, String partOfCorporate,
                                              int orderBy, String orderByType);

    void deactivateCustomer(long id);

    void deactivateCustomerTariff(long id);

    List<Customer> getCustomersByCorporate(long corporateId);

    Map<String, Object> getAllCustomersSearch(int page, int size, String email, String phone, String surname, int corporate, int region, String status);

    Customer addNewCustomer(Customer customer);

}
