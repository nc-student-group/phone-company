package com.phonecompany.model;

public class Corporate extends DomainEntity {

    private String personalAccountNumber;
    private String corporateName;
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
