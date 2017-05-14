package com.phonecompany.controller;

import com.phonecompany.model.User;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by nik9str on 14.05.2017.
 */
@RestController
@RequestMapping(value = "/api/search")
public class SearchController {


        private static final Logger LOGGER = LoggerFactory.getLogger(com.phonecompany.controller.SearchController.class);
        private UserService userService;

        @Autowired
        public SearchController(UserService userService) {
            this.userService = userService;
        }


        @GetMapping(value = "users/{email}/{role}/{status}")
        public List<User> getUsers(@PathVariable("email") String email,
                                   @PathVariable("role") int userRole,
                                   @RequestParam("status") String status) {
            List<User> users = this.userService.getAllUsersSearch(email, userRole, status);
            return users;
        }
}
