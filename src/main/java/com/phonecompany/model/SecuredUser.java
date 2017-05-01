package com.phonecompany.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecuredUser {

    private User user;

    public SecuredUser(Object user) {
        this.user = (org.springframework.security.core.userdetails.User) user;
    }

    public String getUserName() {
        return user.getUsername();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public Collection<GrantedAuthority> getAuthorities(User user) {
        return user.getAuthorities();
    }

    @Override
    public String toString() {
        return "SecuredUser{" +
                "user=" + user +
                '}';
    }
}
