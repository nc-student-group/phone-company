package com.phonecompany.controller;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

        /**
         * All the actions described below are done due to presence
         * of the {@code RestController} annotation placed above
         * the class.
         *
         * Depending on the Content-Type specified in the header of
         * an HTTP request, Spring will look for a corresponding
         * {@code MessageConverter} implementation in order to
         * provide a response represented in the requested format.
         * For instance, for application/json it will be
         * {@code MappingJackson2HttpMessageConverter} that can
         * convert Java objects into the JSON response and back.
         */
        return Collections.unmodifiableList(users);
    }

    @RequestMapping(method = POST, value = "/api/users")
    /**
     * When Spring sees {@code @RequestBody} annotation it loops
     * through the {@code MessageConverter}s in order to find
     * a suitable one(depending on the Content-Type header)
     * and converts incoming JSON payload to the respective Java
     * object
     */
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

        /**
         * ResponseEntity is meant to represent the entire HTTP response.
         * It wires status codes to the response in order to provide
         * user with an information regarding the result of his request.
         */
        return new ResponseEntity<>(persistedUser, httpHeaders, HttpStatus.CREATED);
    }
}
