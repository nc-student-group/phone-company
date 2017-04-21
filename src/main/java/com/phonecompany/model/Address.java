package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class Address extends DomainEntity {
    @NotNull(message = "Region must not be null")
    private String region;
    @NotNull(message = "Locality must not be null")
    private String locality;
    @NotNull(message = "Street must not be null")
    private String street;
    @NotNull(message = "House number must not be null")
    private String houseNumber;
    @NotNull(message = "Apartment number must not be null")
    private String apartmentNumber;

    public Address() {
        super();
    }

    public Address(String region, String locality,
                   String street, String houseNumber,
                   String apartmentNumber) {
        this.region = region;
        this.locality = locality;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        return "Address{" +
                "region='" + region + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                "} " + super.toString();
    }
}
