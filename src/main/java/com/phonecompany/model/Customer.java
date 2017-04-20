package com.phonecompany.model;

import com.phonecompany.model.enums.CustomerCategory;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;

import java.util.List;

public class Customer extends User {

    private String firstName;
    private String secondName;
    private String lastName;
    private String phone;
    private List<Customer> representatives;
    private CustomerCategory customerCategory;

    public Customer(String email, String password,
                    UserRole role, Customer representative,
                    Status status, String firstName,
                    String secondName, String lastName,
                    String phone, List<Customer> representatives) {
        super(email, password, role, representative, status);
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.phone = phone;
        this.representatives = representatives;
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

    public List<Customer> getRepresentatives() {
        return representatives;
    }

    public void setRepresentatives(List<Customer> representatives) {
        this.representatives = representatives;
    }
}
