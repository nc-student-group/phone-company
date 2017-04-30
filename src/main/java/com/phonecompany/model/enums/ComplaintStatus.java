package com.phonecompany.model.enums;

public enum ComplaintStatus {
    ACCEPTED,
    INTRAPROCESS,
    ACCOMPLISHED;

    @Override
    public String toString() {
        return this.name();
    }
}
