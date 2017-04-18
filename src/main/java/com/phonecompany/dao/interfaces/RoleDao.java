package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Role;

import java.util.List;

public interface RoleDao extends CrudDao<Role> {
    List<Role> getAllForAdmin();

}
