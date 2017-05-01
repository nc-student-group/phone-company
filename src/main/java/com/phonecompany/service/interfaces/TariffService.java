package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.model.enums.ProductStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TariffService extends CrudService<Tariff> {
    public List<Tariff> getByRegionIdAndPaging(long regionId, int page, int size);

    public List<Tariff> getByRegionIdAndClient(Long regionId, Boolean isRepresentative);

    public Integer getCountByRegionIdAndPaging(long regionId);

    public Map<String, Object> getTariffsTable(long regionId, int page, int size);

    public void updateTariffStatus(long tariffId, ProductStatus productStatus);

    public Tariff findByTariffName(String tariffName);

    public ResponseEntity<?> updateTariffAndRegions(List<TariffRegion> tariffRegions);

    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size);

    public Integer getCountTariffsAvailableForCustomer(long regionId);

    public List<Tariff> getTariffsAvailableForCorporate(int page, int size);

    public Integer getCountTariffsAvailableForCorporate();

    public Tariff getByIdForSingleCustomer(long id, long regionId);

    public void deactivateTariff(CustomerTariff customerTariff);

    public void activateTariff(Customer customer, TariffRegion tariffRegion);
}
