package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.Address;
import com.phonecompany.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends CrudServiceImpl<Address> implements AddressService {

    @Autowired
    public AddressServiceImpl(CrudDao<Address> dao) {
        super(dao);
    }

}
