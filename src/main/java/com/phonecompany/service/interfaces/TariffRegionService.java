package com.phonecompany.service.interfaces;

import com.phonecompany.model.TariffRegion;

import java.util.List;

public interface TariffRegionService extends CrudService<TariffRegion> {
    public List<TariffRegion> getAllByTariffId(Long tariffId);
    public void deleteByTariffId(long tariffId);
    public TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId);
}
