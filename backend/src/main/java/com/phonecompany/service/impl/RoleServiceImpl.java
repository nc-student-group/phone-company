package com.phonecompany.service.impl;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.dao.interfaces.RoleDao;
import com.phonecompany.model.Role;
import com.phonecompany.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Yurii on 14.04.2017.
 */
@Service
public class RoleServiceImpl extends AbstractServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao dao) {
        super(dao);
    }
}
