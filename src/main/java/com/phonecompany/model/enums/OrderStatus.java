package com.phonecompany.model.enums;

public enum OrderStatus {
    CREATED,
    DONE,
    CANCELED;

    @Override
    public String toString() {
        return this.name();
    }
}
