package com.phonecompany.dao.interfaces;

import com.phonecompany.model.CustomerTariff;

import java.util.List;

public interface CustomerTariffDao extends CrudDao<CustomerTariff>{

    List<CustomerTariff> getTariffsByCustomerId(Long customerId);

}
