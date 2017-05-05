package com.phonecompany.service.interfaces;

import com.phonecompany.model.TariffRegion;

import java.util.List;
import java.util.Map;

public interface TariffRegionService extends CrudService<TariffRegion> {
    public List<TariffRegion> getAllByTariffId(Long tariffId);
    public void deleteByTariffId(long tariffId);
    public TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId);
    public Map<String, Object> getTariffsTable(long regionId, int page, int size);
}
