package com.phonecompany.controller;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.EMailService;
import com.phonecompany.service.interfaces.UserService;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private EMailService emailService;

    @Autowired
    public UserController(UserService userService, EMailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @RequestMapping(method = GET, value = "/api/users")
    public Collection<User> getAllUsers() {
        LOG.info("Retrieving all the users contained in the database");

        List<User> users = this.userService.getAll();

        LOG.info("Users fetched from the database: " + users);

        return Collections.unmodifiableCollection(users);
    }

    @RequestMapping(method = POST, value = "/api/users")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        LOG.info("User retrieved from the http request: " + user);

        User persistedUser = this.userService.save(user);
        LOG.info("User persisted with an id: " + persistedUser.getId());

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(persistedUser.getId())
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriOfNewResource);

        return new ResponseEntity<>(persistedUser, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = POST, value = "/api/user/reset")
    public void resetPassword(@RequestBody String email) {
        LOG.info("Trying to reset password for user with email: " + email);
        User user = userService.findByUsername(email);
        if(user != null) {
            userService.resetPassword(user);
            emailService.sendMail(user.getEmail(), "Your new password is " +
                    user.getPassword(), "Reset password");
            LOG.info("User's new password " + user.getPassword());

        } else {
            LOG.info("User with email " + email + " not found!" );
        }
    }


}
