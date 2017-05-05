package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;
import com.phonecompany.model.enums.ProductStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TariffService extends CrudService<Tariff> {
    public List<Tariff> getByRegionIdAndPaging(long regionId, int page, int size);

    public List<Tariff> getByRegionIdAndClient(Long regionId, Boolean isRepresentative);

    public Integer getCountByRegionId(long regionId);

    public void updateTariffStatus(long tariffId, ProductStatus productStatus);

    public Tariff findByTariffName(String tariffName);

    public Map<String, Object> getTariffsAvailableForCustomer(int page, int size);

    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size);

    public Integer getCountTariffsAvailableForCustomer(long regionId);

    public List<Tariff> getTariffsAvailableForCorporate(int page, int size);

    public Integer getCountTariffsAvailableForCorporate();

    public Tariff getByIdForSingleCustomer(long id, long regionId);

    public void deactivateSingleTariff(CustomerTariff customerTariff);

    public void deactivateCorporateTariff(CustomerTariff customerTariff);

    public void activateSingleTariff(Customer customer, TariffRegion tariffRegion);

    public void activateCorporateTariff(Corporate corporate, Tariff tariff);

    public void activateTariff(long tariffId);

    public Tariff addNewTariff(List<TariffRegion> tariffRegions);

    public Tariff addNewTariff(Tariff tariff);

    public Tariff updateTariff(List<TariffRegion> tariffRegions);

    public Tariff updateTariff(Tariff tariff);

    public Tariff getTariffForCustomer(long tariffId);

}
