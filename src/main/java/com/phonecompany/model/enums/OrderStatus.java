package com.phonecompany.model.enums;

public enum OrderStatus {
    CREATED,
    DONE,
    PENDING,
    CANCELED;

    @Override
    public String toString() {
        return this.name();
    }
}
