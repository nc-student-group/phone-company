package com.phonecompany.model.enums;

public enum ComplaintStatus implements ItemType {
    ACCEPTED,
    INTRAPROCESS,
    ACCOMPLISHED;

    @Override
    public String toString() {
        return this.name();
    }
}
