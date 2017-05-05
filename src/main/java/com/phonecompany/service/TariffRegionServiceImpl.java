package com.phonecompany.service;

import com.phonecompany.dao.interfaces.TariffRegionDao;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.service.interfaces.TariffRegionService;
import com.phonecompany.service.interfaces.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TariffRegionServiceImpl extends CrudServiceImpl<TariffRegion> implements TariffRegionService{

    private TariffRegionDao tariffRegionDao;
    private TariffService tariffService;

    @Autowired
    public TariffRegionServiceImpl(TariffRegionDao tariffRegionDao){
        super(tariffRegionDao);
        this.tariffRegionDao = tariffRegionDao;
    }

    @Override
    public List<TariffRegion> getAllByTariffId(Long tariffId){
        return tariffRegionDao.getAllByTariffId(tariffId);
    }

    @Override
    public void deleteByTariffId(long tariffId){
        this.tariffRegionDao.deleteByTariffId(tariffId);
    }

    @Override
    public TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId){
        return this.tariffRegionDao.getByTariffIdAndRegionId(tariffId, regionId);
    }

    @Override
    public Map<String, Object> getTariffsTable(long regionId, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<Tariff> tariffs = tariffService.getByRegionIdAndPaging(regionId, page, size);
        List<Object> rows = new ArrayList<>();
        tariffs.forEach((Tariff tariff) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("tariff", tariff);
            row.put("regions", this.getAllByTariffId(tariff.getId()));
            rows.add(row);
        });
        response.put("tariffs", rows);
        response.put("tariffsSelected", tariffService.getCountByRegionId(regionId));
        return response;
    }
}
