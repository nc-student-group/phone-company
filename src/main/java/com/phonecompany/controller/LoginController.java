package com.phonecompany.controller;

import com.phonecompany.model.Role;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.RoleService;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.util.List;

@RestController
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    private RoleService roleService;
    private UserService userService;

    @Autowired
    public LoginController(RoleService roleService,
                           UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @RequestMapping(value = "/api/roles", method = RequestMethod.GET)
    public List<Role> getAllRoles() {
        return roleService.getAll();
    }

    @RequestMapping(value = "/api/login/try", method = RequestMethod.GET)
    public Role tryLogin() {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(securityUser.getUsername());
        return user.getRole();
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public Role login(@RequestParam("firstName") String firstName,
                      @RequestParam("password") String password) {
        LOG.info("AuthorizationRequest: {}", firstName);
        User user = userService.findByUsername(firstName);
        LOG.info("User retrieved from the database: {}", user);
        if(user == null) return null;
        if(!user.getPassword().equals(password)) return null;
        LOG.info("User role: {}", user.getRole());
        return user.getRole();
    }

    @RequestMapping(value = "/api/users/all", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @RequestMapping(value = "/api/user/get", method = RequestMethod.GET)
    public User getUserById(@RequestParam("id") long id) {
        return userService.getById(id);
    }

    @RequestMapping(value = "/api/user/update", method = RequestMethod.POST)
    public User updateUser(@RequestBody User user) {
        userService.update(user);
        return user;
    }


}
