package com.phonecompany.model.enums;

import com.phonecompany.model.enums.interfaces.ItemType;

public enum OrderType implements ItemType {
    ACTIVATION,
    SUSPENSION,
    RESUMING,
    DEACTIVATION
}
