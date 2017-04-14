package com.phonecompany.service;

import com.phonecompany.service.interfaces.UserService;
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
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        com.phonecompany.model.User user = userService.findByUsername(login);
        if (user == null)
            throw new UsernameNotFoundException(login + " not found");

        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().getName()));

        return new User(user.getEmail(), user.getPassword(), roles);
    }
}
