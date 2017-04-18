package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.dao.interfaces.RoleDao;
import com.phonecompany.model.Role;
import com.phonecompany.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends CrudServiceImpl<Role> implements RoleService {

    private RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao dao) {
        super(dao);
        this.roleDao = dao;
    }

    @Override
    public List<Role> getAllForAdmin(){
        return roleDao.getAllForAdmin();
    }

}
