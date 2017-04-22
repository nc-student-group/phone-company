package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.model.Corporate;
import com.phonecompany.service.interfaces.CorporateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorporateServiceImpl extends CrudServiceImpl<Corporate> implements CorporateService {

    private CorporateDao corporateDao;

    @Autowired
    public CorporateServiceImpl(CorporateDao corporateDao){
        super(corporateDao);
        this.corporateDao = corporateDao;
    }
}
