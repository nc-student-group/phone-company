package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.Role;
import com.phonecompany.service.interfaces.RoleService;

public class RoleServiceImpl extends CrudServiceImpl<Role> implements RoleService{

    public RoleServiceImpl(CrudDao<Role> dao) {
        super(dao);
    }

}
