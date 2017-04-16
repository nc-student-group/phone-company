package com.phonecompany.service;

import com.phonecompany.model.Role;
import com.phonecompany.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yurii on 14.04.2017.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        logger.info(login);
        com.phonecompany.model.User user = userService.findByUsername(login);
        if (user == null){
            throw new UsernameNotFoundException(login + " not found");
//            user = new com.phonecompany.model.User("bad-credentials", "", new Role("NOT_AUTHORIZE"));
        }

        logger.info(user.getEmail());
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().getName()));

        return new User(user.getEmail(), user.getPassword(), roles);
    }
}