package com.phonecompany.service;

import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.model.Service;
import com.phonecompany.service.interfaces.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends CrudServiceImpl<Service> implements ServiceService {

    private ServiceDao serviceDao;

    @Autowired
    public ServiceServiceImpl(ServiceDao serviceDao){
        super(serviceDao);
        this.serviceDao = serviceDao;
    }
}
