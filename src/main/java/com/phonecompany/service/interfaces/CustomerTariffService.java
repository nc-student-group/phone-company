package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;

import java.util.List;

public interface CustomerTariffService extends CrudService<CustomerTariff> {

    List<CustomerTariff> getByClientId(Customer customer);
    public CustomerTariff getCurrentCustomerTariff(long customerId);
    public CustomerTariff getCurrentCorporateTariff(long corporateId);
    CustomerTariff getCurrentActiveOrSuspendedClientTariff(Customer customer);
    CustomerTariff deactivateCustomerTariff(CustomerTariff customerTariff);


}
