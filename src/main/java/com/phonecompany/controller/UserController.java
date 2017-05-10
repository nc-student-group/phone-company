package com.phonecompany.controller;

import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.events.OnUserCreationEvent;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

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
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        LOG.info("User parsed from the request body: " + user);
        User foundedUser = userService.findByEmail(user.getEmail());
        if (foundedUser != null && !foundedUser.getId().equals(user.getId())) {
            return new ResponseEntity<Object>(new Error("User with \"" + user.getEmail() + "\" already exist!"), HttpStatus.CONFLICT);
        }
        userService.update(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @RequestMapping(method = GET, value = "/api/user/get")
    public User getUser() {
        User loggedInUser = this.userService.getCurrentlyLoggedInUser();
        LOG.debug("User retrieved from security context: {}", loggedInUser);

        return loggedInUser;
    }

    @RequestMapping(value = "/api/login/try", method = RequestMethod.GET)
    public ResponseEntity<?> tryLogin() {
        User loggedInUser = this.userService.getCurrentlyLoggedInUser();
        LOG.debug("Currently logged in user: {}", loggedInUser);
        return new ResponseEntity<>(loggedInUser.getRole(), HttpStatus.OK);
    }

    @RequestMapping(method = POST, value = "/api/user/save")
    public ResponseEntity<?> saveUserByAdmin(@RequestBody User user) {
        LOG.info(user.toString());
        if (userService.findByEmail(user.getEmail()) == null) {
            user.setPassword(new BigInteger(50, new SecureRandom()).toString(32));
            eventPublisher.publishEvent(new OnUserCreationEvent(user));
        }
        User persistedUser = this.userService.save(user);
        return new ResponseEntity<>(persistedUser, HttpStatus.CREATED);
    }

    @RequestMapping(method = POST, value = "/api/user/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody HashMap<String,String> pass) {
        userService.changePassword(pass.get("oldPass"),pass.get("newPass"));
        return new ResponseEntity<>(HttpStatus.OK);
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

    @RequestMapping(method = GET, value = "/api/users/{page}/{size}/{role}/{status}")
    public Map<String, Object> getAllUsers(@PathVariable("page") int page, @PathVariable("size") int size,
                                           @PathVariable("role") int userRole, @PathVariable("status") String status) {
        LOG.info("Retrieving all the paginated users contained in the database");

        List<User> users = this.userService.getAllUsersPaging(page, size, userRole, status);

        LOG.info("Users fetched from the database: " + users);
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("usersSelected", userService.getCountUsers(userRole, status));
        return response;
    }

    @RequestMapping(value = "/api/user/update/{id}/{status}", method = RequestMethod.GET)
    public ResponseEntity<Void> updateUserStatus(@PathVariable("id") long id, @PathVariable("status") Status status) {
        userService.updateStatus(id, status);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
