package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
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

@ServiceStereotype
public class TariffRegionServiceImpl extends CrudServiceImpl<TariffRegion>
        implements TariffRegionService{

    private TariffRegionDao tariffRegionDao;

    @Autowired
    public TariffRegionServiceImpl(TariffRegionDao tariffRegionDao){
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

}
