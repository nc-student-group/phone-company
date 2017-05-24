package com.phonecompany.model.enums;

/**
 * Defines statuses of user's account
 */
public enum Status {
    //user state before account verification
    INACTIVE,
    //state after following an email verification link
    ACTIVATED,
    //state if user chooses to be deactivated
    DEACTIVATED
}
