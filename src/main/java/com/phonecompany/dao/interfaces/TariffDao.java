package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;

import java.util.List;

public interface TariffDao extends AbstractPageableDao<Tariff> {

    public List<Tariff> getByRegionId(Long regionId);

    public void updateTariffStatus(long tariffId, ProductStatus productStatus);

    public Tariff findByTariffName(String tariffName);

    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size);

    public List<Tariff> getTariffsAvailableForCustomer(long regionId);

    public Integer getCountTariffsAvailableForCustomer(long regionId);

    public List<Tariff> getTariffsAvailableForCorporate(int page, int size);

    public List<Tariff> getTariffsAvailableForCorporate();

    public Integer getCountTariffsAvailableForCorporate();

    public Tariff getByIdForSingleCustomer(long id, long regionId);
}
