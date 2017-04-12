package com.phonecompany.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines user roles
 */
@Getter
@AllArgsConstructor
public enum UserRole {
    /* Can create users with such roles as: CLIENT, CSR, PMG */
    ADMIN("ADMIN"),
    /* Will post complaints or make requests to the system regarding
    details of the service */
    CLIENT("CLIENT"),
    /**/
    CSR("CSR"),
    /**/
    PMG("PMG");

    private String title;
}
