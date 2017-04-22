package com.phonecompany.service;

import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.model.Tariff;
import com.phonecompany.service.interfaces.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffServiceImpl extends CrudServiceImpl<Tariff> implements TariffService {

    private TariffDao tariffDao;

    @Autowired
    public TariffServiceImpl(TariffDao tariffDao){
        super(tariffDao);
        this.tariffDao = tariffDao;
    }

}
