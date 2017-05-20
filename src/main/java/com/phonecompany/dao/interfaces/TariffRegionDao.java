package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;

import java.util.List;

public interface TariffRegionDao extends JdbcOperations<TariffRegion> {
    List<TariffRegion> getAllByTariffId(Long tariffId);
    TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId);
    void deleteByTariffId(long tariffId);
}
