package com.phonecompany.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Address extends DomainEntity {

    @NotNull(message = "Region must not be null")
    private Region region;
    @Pattern(regexp = "[a-zA-Z]{3,}", message = "Locality can only contain letters")
    @NotNull(message = "Locality must not be null")
    private String locality;
    @Pattern(regexp = "[a-zA-Z]{3,}", message = "Street can only contain letters")
    @NotNull(message = "Street must not be null")
    private String street;
    @NotNull(message = "House number must not be null")
    private String houseNumber;
    @Pattern(regexp = "^[0-9]+$", message = "Apartment number should only be a positive integer")
    @NotNull(message = "Apartment number must not be null")
    private String apartmentNumber;

    public Address() {
        super();
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
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                "} " + super.toString();
    }
}
