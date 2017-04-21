package com.phonecompany.model;

import com.phonecompany.model.enums.ProductStatus;

import javax.validation.constraints.NotNull;

public class Tariff extends DomainEntity {
    @NotNull(message = "Tariff name must not be null")
    private String tariffName;
    @NotNull(message = "Product status must not be null")
    private ProductStatus productStatus;
    @NotNull(message = "Internet must not be null")
    private String internet;
    @NotNull(message = "Calls in network must not be null")
    private String callsInNetwork;
    @NotNull(message = "Calls on other numbers must not be null")
    private String callsOnOtherNumbers;
    @NotNull(message = "Sms must not be null")
    private String sms;
    @NotNull(message = "Mms must not be null")
    private String mms;

    public Tariff() {
    }

    public Tariff(String tariffName, ProductStatus productStatus, String internet, String callsInNetwork, String callsOnOtherNumbers, String sms, String mms) {
        this.tariffName = tariffName;
        this.productStatus = productStatus;
        this.internet = internet;
        this.callsInNetwork = callsInNetwork;
        this.callsOnOtherNumbers = callsOnOtherNumbers;
        this.sms = sms;
        this.mms = mms;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getCallsInNetwork() {
        return callsInNetwork;
    }

    public void setCallsInNetwork(String callsInNetwork) {
        this.callsInNetwork = callsInNetwork;
    }

    public String getCallsOnOtherNumbers() {
        return callsOnOtherNumbers;
    }

    public void setCallsOnOtherNumbers(String callsOnOtherNumbers) {
        this.callsOnOtherNumbers = callsOnOtherNumbers;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getMms() {
        return mms;
    }

    public void setMms(String mms) {
        this.mms = mms;
    }
}