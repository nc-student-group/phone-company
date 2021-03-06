package com.phonecompany.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Customer extends User {

    //optional fields
    private String firstName;
    private String secondName;
    private String lastName;
    //fields that have to be validated
    @Pattern(regexp = "^\\+38077[0-9]{7}$", message = "Phone should be in format of +38077#######")
    @NotNull(message = "Phone must not be null")
    private String phone;
    @NotNull(message = "Address must not be null")
    private Address address;
    private Corporate corporate;
    @JsonProperty(value = "isRepresentative")
    private Boolean isRepresentative;
    private Boolean isMailingEnabled = true; // mailing is enabled by default

    public Customer() {
    }

    public Customer(Long id, String email, String password, UserRole role,
                    Status status, String firstName, String secondName,
                    String lastName, String phone, Address address,
                    Corporate corporate, Boolean isRepresentative, Boolean isMailingEnabled) {
        super(id, email, password, role, status);
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.corporate = corporate;
        this.isRepresentative = isRepresentative;
        this.isMailingEnabled = isMailingEnabled;
    }

    public Customer(String email, String password, UserRole role,
                    Status status, String firstName, String secondName,
                    String lastName, String phone, Address address,
                    Corporate corporate, Boolean isRepresentative, Boolean isMailingEnabled) {
        super(email, password, role, status);
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.corporate = corporate;
        this.isRepresentative = isRepresentative;
        this.isMailingEnabled = isMailingEnabled;
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

    public Boolean getIsMailingEnabled() {
        return isMailingEnabled;
    }

    public void setIsMailingEnabled(Boolean mailingEnabled) {
        isMailingEnabled = mailingEnabled;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", corporate=" + corporate +
                ", isRepresentative=" + isRepresentative +
                ", isMailingEnabled=" + isMailingEnabled +
                "} " + super.toString();
    }
}
