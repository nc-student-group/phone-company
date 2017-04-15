package com.phonecompany.controller;

import com.phonecompany.model.Role;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.RoleService;
import com.phonecompany.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/api/roles", method = RequestMethod.GET)
    public List<Role> getAllRoles(){
        return roleService.getAll();
    }
}
