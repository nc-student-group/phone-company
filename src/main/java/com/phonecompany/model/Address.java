package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class Address extends DomainEntity {

    @NotNull(message = "Region must not be null")
    private Long regionId;
    @NotNull(message = "Street must not be null")
    private String street;
    @NotNull(message = "House number must not be null")
    private Long houseNumber;
    @NotNull(message = "Apartment number must not be null")
    private String apartmentNumber;

    public Address() {
        super();
    }

    public Address(Long regionId,
                   String street, Long houseNumber,
                   String apartmentNumber) {
        this.regionId = regionId;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Long houseNumber) {
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
