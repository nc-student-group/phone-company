package com.phonecompany.service.interfaces;

import com.phonecompany.model.TariffRegion;

import java.util.List;

public interface TariffRegionService extends CrudService<TariffRegion> {
    public List<TariffRegion> getAllTariffsByRegionId(Long regionId, int page, int size);
}
