package com.phonecompany.model;

import java.util.List;

public class BusinessCustomer {

    private List<Representative> representative;
    private String secretKey;

    public BusinessCustomer(List<Representative> representative,
                            String secretKey) {
        this.representative = representative;
        this.secretKey = secretKey;
    }

    public List<Representative> getRepresentative() {
        return representative;
    }

    public void setRepresentative(List<Representative> representative) {
        this.representative = representative;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
