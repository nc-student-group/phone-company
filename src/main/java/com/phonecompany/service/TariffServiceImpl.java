package com.phonecompany.service;

import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.TariffRegionService;
import com.phonecompany.service.interfaces.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TariffServiceImpl extends CrudServiceImpl<Tariff> implements TariffService {

    private TariffDao tariffDao;
    private TariffRegionService tariffRegionService;

    @Autowired
    public TariffServiceImpl(TariffDao tariffDao, TariffRegionService tariffRegionService){
        super(tariffDao);
        this.tariffDao = tariffDao;
        this.tariffRegionService = tariffRegionService;
    }

    @Override
    public List<Tariff> getByRegionIdAndPaging(long regionId, int page, int size){
        return tariffDao.getByRegionIdAndPaging(regionId, page, size);
    }

    @Override
    public Integer getCountByRegionIdAndPaging(long regionId){
        return tariffDao.getCountByRegionIdAndPaging(regionId);
    }

    @Override
    public Map<String, Object> getTariffsTable(long regionId, int page, int size){
        Map<String, Object> response = new HashMap<>();
        List<Tariff> tariffs = this.getByRegionIdAndPaging(regionId, page, size);
        List<Object> rows = new ArrayList<>();
        tariffs.forEach((Tariff tariff) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("tariff", tariff);
            row.put("regions", tariffRegionService.getAllByTariffId(tariff.getId()));
            rows.add(row);
        });
        response.put("tariffs",rows);
        response.put("tariffsSelected", this.getCountByRegionIdAndPaging(regionId));
        return response;
    }

    @Override
    public void updateTariffStatus(long tariffId, ProductStatus productStatus){
        this.tariffDao.updateTariffStatus(tariffId, productStatus);
    }

    @Override
    public Tariff findByTariffName(String tariffName){
        return this.tariffDao.findByTariffName(tariffName);
    }

}
