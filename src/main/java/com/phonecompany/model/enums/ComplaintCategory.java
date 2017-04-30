package com.phonecompany.model.enums;

public enum ComplaintCategory {
    TECHNICAL_SERVICE,
    SUGGESTION,
    CUSTOMER_SERVICE;

    @Override
    public String toString() {
        return this.name();
    }
}
