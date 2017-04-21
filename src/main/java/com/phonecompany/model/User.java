package com.phonecompany.model;

import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

public class User extends DomainEntity {

    @NotNull(message = "User have to have email")
    @Email
    private String email;
    @NotNull(message = "User has to have a password")
    private String password;
    @NotNull(message = "User has to have a role")
    private UserRole role;
    @NotNull(message = "User has to have a status")
    private Status status;

    public User() {
    }

    public User(String email, String password,
                UserRole role, Status status) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", status=" + status +
                "} " + super.toString();
    }
}
