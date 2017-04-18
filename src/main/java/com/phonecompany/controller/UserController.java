package com.phonecompany.controller;

import com.phonecompany.model.OnRegistrationCompleteEvent;
import com.phonecompany.model.ResetPasswordEvent;
import com.phonecompany.model.Role;
import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.net.URI;
import java.util.*;

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

    @RequestMapping(method = POST, value = "/api/user/reset")
    public Map<String, String> resetPassword(@RequestBody String email) {
        LOG.info("Trying to reset password for user with email: " + email);
        User userToReset = userService.findByUsername(email);
        Map<String, String> response = new HashMap<>();
        if (userToReset != null) {
            this.eventPublisher.publishEvent(new ResetPasswordEvent(userToReset));
            response.put("msg","success");
        } else {
            LOG.info("User with email " + email + " not found!");
            response.put("msg", "error");
        }
        return response;
    }

    @RequestMapping(method = POST, value = "/api/admin/users") //TODO: has to be one endpoint: /api/users (make Client default enum role)
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        LOG.info("Employee returned from the http request: {}", user);
        User savedUser = this.userService.save(user);
        userService.resetPassword(new ResetPasswordEvent(user));
        LOG.info(user.getEmail() + " password " + user.getPassword());

        LOG.info("Saved user: {}", savedUser);

        HttpHeaders resourceHeaders = getResourceHeaders(USERS_RESOURCE_NAME, savedUser.getId());

        return new ResponseEntity<>(savedUser, resourceHeaders, HttpStatus.CREATED);
    }
}
