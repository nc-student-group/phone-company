package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.RegionDao;
import com.phonecompany.model.Region;
import com.phonecompany.service.interfaces.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@ServiceStereotype
public class RegionServiceImpl extends CrudServiceImpl<Region> implements RegionService {

    private RegionDao regionDao;

    @Autowired
    public RegionServiceImpl(RegionDao regionDao){
        super(regionDao);
        this.regionDao = regionDao;
    }
}
