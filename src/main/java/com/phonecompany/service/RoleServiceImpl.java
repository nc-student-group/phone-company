package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.Role;
import com.phonecompany.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends CrudServiceImpl<Role> implements RoleService {

    @Autowired
    public RoleServiceImpl(CrudDao<Role> dao) {
        super(dao);
    }

}
