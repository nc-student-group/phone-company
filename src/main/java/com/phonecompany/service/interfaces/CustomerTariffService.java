package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;

import java.util.List;
import java.util.Map;

public interface CustomerTariffService extends CrudService<CustomerTariff> {

    List<CustomerTariff> getByClientId(Customer customer);

    CustomerTariff getCurrentCustomerTariff(Customer customer);

    CustomerTariff getCurrentCustomerTariff(long customerId);

    CustomerTariff getCurrentCorporateTariff(long corporateId);

    CustomerTariff getCurrentActiveOrSuspendedClientTariff(Customer customer);

    CustomerTariff getCurrentActiveOrSuspendedCorporateTariff(long corporateId);

    CustomerTariff deactivateCustomerTariff(CustomerTariff customerTariff);

    CustomerTariff resumeCustomerTariff(CustomerTariff customerTariff);

    CustomerTariff suspendCustomerTariff(Map<String, Object> suspensionData);

    void resumeCustomerTariff(Order order);
}
