package com.phonecompany.dao.interfaces;

import com.phonecompany.model.TariffRegion;

import java.util.List;

public interface TariffRegionDao extends JdbcOperations<TariffRegion> {
    List<TariffRegion> getAllByTariffId(Long tariffId);
    TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId);
    List<TariffRegion> getAllByRegionId(Long regionId);
    void deleteByTariffId(long tariffId);
}
