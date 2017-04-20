package com.phonecompany.model.enums;

public enum Status {
    //user state before account verification
    INACTIVE,
    //state after following an email verification link
    ACTIVATED,
    //state if user chooses to be deactivated
    DEACTIVATED
}
