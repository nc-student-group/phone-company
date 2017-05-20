package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.Query;

import java.util.List;

public interface TariffDao extends JdbcOperations<Tariff> {

    void updateTariffStatus(long tariffId, ProductStatus productStatus);

    Tariff findByTariffName(String tariffName);

    List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size);

    List<Tariff> getTariffsAvailableForCustomer(long regionId);

    Integer getCountTariffsAvailableForCustomer(long regionId);

    List<Tariff> getTariffsAvailableForCorporate(int page, int size);

    List<Tariff> getTariffsAvailableForCorporate();

    Integer getCountTariffsAvailableForCorporate();

    Tariff getByIdForSingleCustomer(long id, long regionId);

    List<Tariff> getAllTariffsSearch(Query query);
}
