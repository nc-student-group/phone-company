package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.AddressDao;
import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.Address;
import com.phonecompany.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@ServiceStereotype
public class AddressServiceImpl extends CrudServiceImpl<Address> implements AddressService {

    private AddressDao addressDao;

    @Autowired
    public AddressServiceImpl(AddressDao dao) {
        super(dao);
        this.addressDao = dao;
    }

}
