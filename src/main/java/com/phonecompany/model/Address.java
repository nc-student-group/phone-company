package com.phonecompany.model;

public class Address extends DomainEntity{

    private String country;
    private String region;
    private String street;
    private String houseNumber;
    private String apartmentNumber;

    public Address() {
        super();
    }

    public Address(String country, String region,
                   String street, String houseNumber,
                   String apartmentNumber) {
        this.country = country;
        this.region = region;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public Address(Long id, String country,
                   String region, String street,
                   String houseNumber, String apartmentNumber) {
        super(id);
        this.country = country;
        this.region = region;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                "} " + super.toString();
    }
}
