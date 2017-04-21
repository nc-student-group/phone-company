package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class Corporate extends DomainEntity {

    @NotNull(message = "Personal account number must not be null")
    private String personalAccountNumber;
    @NotNull(message = "Corporate name number must not be null")
    private String corporateName;
    @NotNull(message = "Corporate name number must not be null")
    private Integer numberOfCorporateSubscribers;

    public Corporate() {

    }

    public Corporate(String personalAccountNumber, String corporateName,
                     Integer numberOfCorporateSubscribers) {
        this.personalAccountNumber = personalAccountNumber;
        this.corporateName = corporateName;
        this.numberOfCorporateSubscribers = numberOfCorporateSubscribers;
    }

    public Corporate(Long id, String personalAccountNumber,
                     String corporateName, Integer numberOfCorporateSubscribers) {
        super(id);
        this.personalAccountNumber = personalAccountNumber;
        this.corporateName = corporateName;
        this.numberOfCorporateSubscribers = numberOfCorporateSubscribers;
    }

    public String getPersonalAccountNumber() {
        return personalAccountNumber;
    }

    public void setPersonalAccountNumber(String personalAccountNumber) {
        this.personalAccountNumber = personalAccountNumber;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public Integer getNumberOfCorporateSubscribers() {
        return numberOfCorporateSubscribers;
    }

    public void setNumberOfCorporateSubscribers(Integer numberOfCorporateSubscribers) {
        this.numberOfCorporateSubscribers = numberOfCorporateSubscribers;
    }

    @Override
    public String toString() {
        return "Corporate{" +
                "personalAccountNumber='" + personalAccountNumber + '\'' +
                ", corporateName='" + corporateName + '\'' +
                ", numberOfCorporateSubscribers=" + numberOfCorporateSubscribers +
                '}' + super.toString();
    }
}
