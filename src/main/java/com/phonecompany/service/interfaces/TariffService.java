package com.phonecompany.service.interfaces;

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
}
