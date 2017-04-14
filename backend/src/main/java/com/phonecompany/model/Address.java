package com.phonecompany.model;

/**
 * Created by Yurii on 14.04.2017.
 */
public class Address extends DomainEntity{
    private String country;
    private String region;
    private String settlement;
    private String street;
    private String houseNumber;
    private String apartment;

    public Address() {
        super();
    }

    public Address(String country, String region, String settlement, String street, String houseNumber, String apartment) {
        this.country = country;
        this.region = region;
        this.settlement = settlement;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartment = apartment;
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

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
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

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
