package com.phonecompany.model;

import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Customer extends User {

    @NotNull(message = "First name must not be null")
    @Pattern(regexp = "[a-zA-Z]{3,}")
    private String firstName;
    @Pattern(regexp = "[a-zA-Z]{3,}")
    @NotNull(message = "Second name must not be null")
    private String secondName;
    @Pattern(regexp = "[a-zA-Z]{3,}")
    @NotNull(message = "Last name must not be null")
    private String lastName;
    @Pattern(regexp = "^\\+[0-9]{12}$")
    @NotNull(message = "Phone must not be null")
    private String phone;
    @NotNull(message = "Address must not be null")
    private Address address;
    private Corporate corporate;
    @NotNull(message = "isRepresentative must not be null")
    private Boolean isRepresentative;

    public Customer() {
    }

    public Customer(String email, String password, UserRole role,
                    Status status, String firstName, String secondName,
                    String lastName, String phone, Address address,
                    Corporate corporate, Boolean isRepresentative) {
        super(email, password, role, status);
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.corporate = corporate;
        this.isRepresentative = isRepresentative;
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
                "} " + super.toString();
    }
}
