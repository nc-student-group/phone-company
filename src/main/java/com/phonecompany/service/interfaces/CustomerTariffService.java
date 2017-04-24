package com.phonecompany.service.interfaces;

import com.phonecompany.model.CustomerTariff;

import java.util.List;

public interface CustomerTariffService extends CrudService<CustomerTariff> {

    List<CustomerTariff> getByCustomerId(Long customerId);

}
