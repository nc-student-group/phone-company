package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;

import java.util.List;

public interface TariffDao extends CrudDao<Tariff> {
    public List<Tariff> getByRegionIdAndPaging(long regionId, int page, int size);
    public List<Tariff> getByRegionId(Long regionId);
    public Integer getCountByRegionIdAndPaging(long regionId);
    public void updateTariffStatus(long tariffId, ProductStatus productStatus);
    public Tariff findByTariffName(String tariffName);
    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size);
}
