package com.phonecompany.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Address extends DomainEntity {

    @NotNull(message = "Region must not be null")
    private Region region;
    @Pattern(regexp = "[a-zA-Z]{3,}", message = "Locality can only contain letters")
    private String locality;
    @Pattern(regexp = "^[a-zA-Z0-9#\\\\ ]+$", message = "Street should not contain special characters")
    private String street;
    @Pattern(regexp = "^[^!@#$%^&*()_+-]*", message = "House number should not contain special characters")
    private String houseNumber;
    @Pattern(regexp = "^[^!@#$%^&*()_+-]*$",
            message = "This field should not contain special characters")
    private String apartmentNumber;

    public Address() {
        super();
    }

    public Address(Long id, Region region, String locality,
                   String street, String houseNumber,
                   String apartmentNumber) {
        super(id);
        this.region = region;
        this.locality = locality;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public Address(Region region, String locality, String street, String houseNumber,
                   String apartmentNumber) {
        this.region = region;
        this.locality = locality;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }


    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "region=" + region +
                ", locality='" + locality + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                "} " + super.toString();
    }
}
