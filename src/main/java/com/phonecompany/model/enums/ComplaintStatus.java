package com.phonecompany.model.enums;

import com.phonecompany.model.enums.interfaces.ItemType;

public enum ComplaintStatus implements ItemType {
    ACCEPTED,
    INTRAPROCESS,
    ACCOMPLISHED;

    @Override
    public String toString() {
        return this.name();
    }
}
