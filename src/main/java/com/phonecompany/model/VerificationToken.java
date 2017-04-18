package com.phonecompany.model;

import java.sql.Date;
import java.util.Calendar;

public class VerificationToken extends DomainEntity{
    private User user;
    private String token;
    private Date expireDate;

    public VerificationToken() {
        super();
    }

    public VerificationToken(User user, String token, Date expireDate) {
        this.user = user;
        this.token = token;
        this.expireDate = expireDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
