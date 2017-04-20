package com.phonecompany.model;

import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;

public class User extends DomainEntity {

    private String email;
    private String password;
    private UserRole role;
    private Customer representative;
    private Status status;

    public User() {
    }

    public User(String email, String password,
                UserRole role, Customer representative,
                Status status) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.representative = representative;
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

    public Customer getRepresentative() {
        return representative;
    }

    public void setRepresentative(Customer representative) {
        this.representative = representative;
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
