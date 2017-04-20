package com.phonecompany.model;

import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;

public class User extends DomainEntity {

    private String email;
    private String password;
    private UserRole role;
    private String firstName;
    private String secondName;
    private String lastName;
    private String phone;
    private Address address;
    private Corporate corporate;
    private Boolean isRepresentative;
    private Status status;

    public User() {
    }

    public User(String email, String password, UserRole role,
                String firstName, String secondName, String lastName,
                String phone, Address address, Corporate corporate,
                Boolean isRepresentative, Status status) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.corporate = corporate;
        this.isRepresentative = isRepresentative;
        this.status = status;
    }

    public User(Long id, String email, String password,
                UserRole role, String firstName, String secondName,
                String lastName, String phone, Address address,
                Corporate corporate, Boolean isRepresentative, Status status) {
        super(id);
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.corporate = corporate;
        this.isRepresentative = isRepresentative;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Corporate getCorporate() {
        return corporate;
    }

    public void setCorporate(Corporate corporate) {
        this.corporate = corporate;
    }

    public Boolean getRepresentative() {
        return isRepresentative;
    }

    public void setRepresentative(Boolean representative) {
        isRepresentative = representative;
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
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", corporate=" + corporate +
                ", isRepresentative=" + isRepresentative +
                ", status=" + status +
                '}' + super.toString();
    }
}
