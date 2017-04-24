package com.phonecompany.controller;

import com.phonecompany.model.OnUserCreationEvent;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.phonecompany.util.RestUtil.getResourceHeaders;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    public static final String USERS_RESOURCE_NAME = "users";

    private UserService userService;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(UserService userService,
                          ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(method = GET, value = "/api/users")
    public Collection<User> getAllUsers() {
        LOG.info("Retrieving all the users contained in the database");

        List<User> users = this.userService.getAll();

        LOG.info("Users fetched from the database: " + users);

        return Collections.unmodifiableCollection(users);
    }

    @RequestMapping(method = POST, value = "/api/user/update")
    public ResponseEntity<?> updateUser(@RequestBody User client) {
        LOG.info(client.toString());
        userService.update(client);

        User persistedUser = this.userService.save(client);
        HttpHeaders resourceHeaders = getResourceHeaders(USERS_RESOURCE_NAME, persistedUser.getId());
        return new ResponseEntity<>(persistedUser, resourceHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = GET, value = "/api/user/get")
    public User getUser() {
        org.springframework.security.core.userdetails.User securityUser = null;
        securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();
        User user = userService.findByEmail(securityUser.getUsername());
        LOG.info("Retrieving all the users contained in the database");

        return user;
    }

    @RequestMapping(value = "/api/login/try", method = RequestMethod.GET)
    public ResponseEntity<?> tryLogin() {
        LOG.debug("About to fetch currently logged in role");
        org.springframework.security.core.userdetails.User securityUser = null;
        securityUser = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();
        User user = userService.findByEmail(securityUser.getUsername());
        return new ResponseEntity<>(user.getRole(), HttpStatus.OK);
    }


    @RequestMapping(method = POST, value = "/api/user/save")
    public ResponseEntity<?> saveUserByAdmin(@RequestBody User user) {

        user.setPassword(new BigInteger(50, new SecureRandom()).toString(32));
        eventPublisher.publishEvent(new OnUserCreationEvent(user));
        User persistedUser = this.userService.save(user);

        HttpHeaders resourceHeaders = getResourceHeaders(USERS_RESOURCE_NAME, persistedUser.getId());
        return new ResponseEntity<>(persistedUser, resourceHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = POST, value = "api/user/reset")
    public ResponseEntity<?> resetPassword(@RequestBody String email) {
        LOG.info("Trying to reset password for user with email: " + email);
        User persistedUser = userService.findByEmail(email);
        if (persistedUser != null) {
            userService.resetPassword(persistedUser);
            LOG.info("User's new password " + persistedUser.getPassword());
        } else {
            LOG.info("User with email " + email + " not found!");
        }
        return new ResponseEntity<>(persistedUser, HttpStatus.OK);
    }
}
