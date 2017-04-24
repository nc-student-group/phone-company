package com.phonecompany.model.enums;

public enum ProductStatus {
    ACTIVATED,
    DEACTIVATED;

    @Override
    public String toString() {
        return this.name();
    }
}
