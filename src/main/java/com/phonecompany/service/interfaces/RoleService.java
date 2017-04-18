package com.phonecompany.service.interfaces;

import com.phonecompany.model.Role;

import java.util.List;

public interface RoleService extends CrudService<Role> {
    List<Role> getAllForAdmin();
}