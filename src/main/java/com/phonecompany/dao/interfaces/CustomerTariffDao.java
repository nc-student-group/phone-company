package com.phonecompany.dao.interfaces;

import com.phonecompany.model.CustomerTariff;

import java.util.List;

public interface CustomerTariffDao extends JdbcOperations<CustomerTariff> {

    List<CustomerTariff> getCustomerTariffsByCustomerId(Long customerId);
    List<CustomerTariff> getCustomerTariffsByCorporateId(Long corporateId);
    CustomerTariff getCurrentCustomerTariff(long customerId);
    CustomerTariff getCurrentCorporateTariff(long corporateId);
    CustomerTariff getCurrentActiveOrSuspendedCustomerTariff(long customerId);
    CustomerTariff getCurrentActiveOrSuspendedCorporateTariff(long corporateId);

}
