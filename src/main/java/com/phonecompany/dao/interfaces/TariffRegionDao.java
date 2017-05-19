package com.phonecompany.dao.interfaces;

import com.phonecompany.model.TariffRegion;

import java.util.List;

public interface TariffRegionDao extends JdbcOperations<TariffRegion> {
    public List<TariffRegion> getAllByTariffId(Long tariffId);
    public TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId);
    public void deleteByTariffId(long tariffId);
}
