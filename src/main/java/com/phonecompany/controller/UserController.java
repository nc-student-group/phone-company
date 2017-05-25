package com.phonecompany.controller;

import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.events.OnUserCreationEvent;
import com.phonecompany.service.email.customer_related_emails.ResetPasswordEmailCreator;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private ApplicationEventPublisher eventPublisher;
    private EmailService<User> emailService;
    private ResetPasswordEmailCreator resetPassMessageCreator;

    @Autowired
    public UserController(UserService userService, ApplicationEventPublisher eventPublisher,
                          ResetPasswordEmailCreator resetPassMessageCreator, EmailService<User> emailService) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.resetPassMessageCreator = resetPassMessageCreator;
        this.emailService = emailService;
    }


    @GetMapping("/api/users")
    public Collection<User> getAllUsers() {
        LOG.info("Retrieving all the users contained in the database");

        List<User> users = this.userService.getAll();

        LOG.info("Users fetched from the database: " + users);

        return users;
    }

    @PutMapping("/api/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        LOG.info("User parsed from the request body: " + user);
        User foundedUser = userService.findByEmail(user.getEmail());
        if (foundedUser != null && !foundedUser.getId().equals(user.getId())) {
            return new ResponseEntity<Object>(new Error("User with \"" + user.getEmail() + "\" already exist!"), HttpStatus.CONFLICT);
        }
        userService.update(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/api/users/logged-in-user")
    public User getUser() {
        User loggedInUser = this.userService.getCurrentlyLoggedInUser();
        LOG.debug("User retrieved from security context: {}", loggedInUser);

        return loggedInUser;
    }

    @GetMapping("/api/login/try")
    public ResponseEntity<?> tryLogin() {
        User loggedInUser;
        try {
            loggedInUser = this.userService.getCurrentlyLoggedInUser();
        } catch (ClassCastException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        LOG.debug("Currently logged in user: {}", loggedInUser);
        return new ResponseEntity<>(loggedInUser.getRole(), HttpStatus.OK);
    }

    @PostMapping("/api/users")
    public ResponseEntity<?> saveUserByAdmin(@RequestBody User user) {
        LOG.info(user.toString());
        if (userService.findByEmail(user.getEmail()) == null) {
            user.setPassword(new BigInteger(50, new SecureRandom()).toString(32));
            eventPublisher.publishEvent(new OnUserCreationEvent(user));
        }
        User persistedUser = this.userService.save(user);
        return new ResponseEntity<>(persistedUser, HttpStatus.CREATED);
    }

    @PostMapping("/api/user/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody HashMap<String, String> pass) {
        userService.changePassword(pass.get("oldPass"), pass.get("newPass"),
                userService.getCurrentlyLoggedInUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("api/user/reset")
    public ResponseEntity<?> resetPassword(@RequestBody String email) {
        LOG.info("Trying to reset password for user with email: " + email);
        User persistedUser = userService.findByEmail(email);
        if (persistedUser != null) {
            String newPassword = userService.resetPassword(persistedUser);
            sendResetPasswordMessage(persistedUser, newPassword);
            LOG.info("User's new password " + persistedUser.getPassword());
        } else {
            LOG.info("User with email " + email + " not found!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(persistedUser, HttpStatus.OK);
    }

    @GetMapping("/api/users/{page}/{size}/{role}/{status}")
    public Map<String, Object> getAllUsers(@PathVariable("page") int page, @PathVariable("size") int size,
                                           @PathVariable("role") int userRole, @PathVariable("status") String status,
                                           @RequestParam("em") String email, @RequestParam("ob") int orderBy,
                                           @RequestParam("obt") String orderByType) {
        LOG.info("Retrieving all the paginated users contained in the database");
        Map<String, Object> response = this.userService.getAllUsersPaging(page, size, userRole, status, email,
                orderBy, orderByType);
        LOG.info("Users fetched from the database: " + response.get("users"));
        return response;
    }

    @PatchMapping("/api/user/update/{id}/{status}")
    public ResponseEntity<Void> updateUserStatus(@PathVariable("id") long id, @PathVariable("status") Status status) {
        userService.updateStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    private void sendResetPasswordMessage(User user, String password) {
        SimpleMailMessage resetPasswordMessage =
                this.resetPassMessageCreator.constructMessage(password);
        LOG.info("Sending email reset password to: {}", user.getEmail());
        emailService.sendMail(resetPasswordMessage, user);
    }
}
