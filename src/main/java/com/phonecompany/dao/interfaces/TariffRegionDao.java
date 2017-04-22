package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;

import java.util.List;

public interface TariffRegionDao extends CrudDao<TariffRegion> {
    public List<TariffRegion> getAllTariffsByRegionId(Long regionId, int page, int size);
}
