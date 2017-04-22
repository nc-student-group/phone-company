package com.phonecompany.service;

import com.phonecompany.dao.interfaces.TariffRegionDao;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.service.interfaces.TariffRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffRegionServiceImpl extends CrudServiceImpl<TariffRegion> implements TariffRegionService{

    private TariffRegionDao tariffRegionDao;

    @Autowired
    public TariffRegionServiceImpl(TariffRegionDao tariffRegionDao){
        super(tariffRegionDao);
        this.tariffRegionDao = tariffRegionDao;
    }

    @Override
    public List<TariffRegion> getAllTariffsByRegionId(Long regionId, int page, int size){
        return this.tariffRegionDao.getAllTariffsByRegionId(regionId, page, size);
    }
}
