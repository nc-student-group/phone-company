package com.phonecompany.controller;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(method = GET, value = "/api/users")
    public Collection<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "test1@gmail.com"));
        users.add(new User(2L, "test2@gmail.com"));

        return Collections.unmodifiableList(users);
    }

    @RequestMapping(method = POST, value = "/api/users")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        LOG.info("User retrieved from the http request: " + user);

        User persistedUser = this.userDao.save(user);
        LOG.info("User persisted with an id: " + persistedUser.getId());

        // creating reference to the newly created resource
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(persistedUser.getId())
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriOfNewResource);

        return new ResponseEntity<>(persistedUser, httpHeaders, HttpStatus.CREATED);
    }
}
