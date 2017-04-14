package com.phonecompany.service.impl;

import com.phonecompany.dao.interfaces.AddressDao;
import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.Address;
import com.phonecompany.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Yurii on 14.04.2017.
 */
@Service
public class AddressServiceImpl extends AbstractServiceImpl<Address> implements AddressService{

    @Autowired
    private AddressDao addressDao;

    @Autowired
    public AddressServiceImpl(AddressDao dao) {
        super(dao);
    }
}
