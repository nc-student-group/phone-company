package com.phonecompany.service.interfaces;

import com.phonecompany.model.TariffRegion;

import java.util.List;
import java.util.Map;

public interface TariffRegionService extends CrudService<TariffRegion> {
    List<TariffRegion> getAllByTariffId(Long tariffId);

    void deleteByTariffId(long tariffId);

    TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId);

    List<TariffRegion> getAllByRegionId(Long regionId);
}
