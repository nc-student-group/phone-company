package com.phonecompany.model.enums;

public enum ComplaintCategory implements ItemType {
    TECHNICAL_SERVICE,
    SUGGESTION,
    CUSTOMER_SERVICE;

    @Override
    public String toString() {
        return this.name();
    }
}
