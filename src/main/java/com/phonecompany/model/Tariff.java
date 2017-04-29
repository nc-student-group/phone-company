package com.phonecompany.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.phonecompany.model.enums.ProductStatus;

import javax.validation.constraints.NotNull;
import java.sql.Date;

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
    @NotNull(message = "Roaming must not be null")
    private String roaming;
    @NotNull(message = "IsCorporate must not be null")
    @JsonProperty(value = "isCorporate")
    private boolean isCorporate;
    @NotNull(message = "Creation date must not be null")
    private Date creationDate;
    private Double discount;
    private String pictureUrl;

    public Tariff() {
    }

    public Tariff(Long id, String tariffName, ProductStatus productStatus, String internet,
                  String callsInNetwork, String callsOnOtherNumbers, String sms, String mms,
                  String roaming, boolean isCorporate, Date creationDate, Double discount, String pictureUrl) {
        super(id);
        this.tariffName = tariffName;
        this.productStatus = productStatus;
        this.internet = internet;
        this.callsInNetwork = callsInNetwork;
        this.callsOnOtherNumbers = callsOnOtherNumbers;
        this.sms = sms;
        this.mms = mms;
        this.roaming = roaming;
        this.isCorporate = isCorporate;
        this.creationDate = creationDate;
        this.discount = discount;
        this.pictureUrl = pictureUrl;
    }
    public boolean getIsCorporate() {  return isCorporate; }

    public void setIsCorporate(boolean corporate) { this.isCorporate = corporate; }

    public String getRoaming() {
        return roaming;
    }

    public void setRoaming(String roaming) {
        this.roaming = roaming;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public void setCorporate(boolean corporate) {
        isCorporate = corporate;
    }

    public Double getDiscount() { return discount; }

    public void setDiscount(Double discount) { this.discount = discount; }

    public String getPictureUrl() { return pictureUrl; }

    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    @Override
    public String toString() {
        return "Tariff{" +
                "tariffName='" + tariffName + '\'' +
                ", productStatus=" + productStatus +
                ", internet='" + internet + '\'' +
                ", callsInNetwork='" + callsInNetwork + '\'' +
                ", callsOnOtherNumbers='" + callsOnOtherNumbers + '\'' +
                ", sms='" + sms + '\'' +
                ", mms='" + mms + '\'' +
                ", roaming='" + roaming + '\'' +
                ", isCorporate=" + isCorporate +
                ", creationDate=" + creationDate +
                ", discount=" + discount +
                ", pictureUrl=" + pictureUrl +
                "} " + super.toString();
    }
}
