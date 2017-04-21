package com.phonecompany.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class VerificationToken extends DomainEntity {

    @NotNull(message = "User must not be null")
    private User user;
    @NotNull(message = "Token must not be null")
    private String token;
    @NotNull(message = "Expire date must not be null")
    private LocalDate expireDate;

    public VerificationToken() {
        super();
    }

    public VerificationToken(User user, String token) {
        this.user = user;
        this.token = token;
        this.expireDate = this.calculateExpiryDate();
    }

    public VerificationToken(Long id, User user, String token,
                             LocalDate expireDate) {
        super(id);
        this.user = user;
        this.token = token;
        this.expireDate = expireDate;
    }

    private LocalDate calculateExpiryDate() {
        return LocalDate.now().plusDays(1);
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

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "user=" + user +
                ", token='" + token + '\'' +
                ", expireDate=" + expireDate +
                "} " + super.toString();
    }
}
