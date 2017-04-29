package com.phonecompany.model.enums;

import static com.phonecompany.model.enums.UserRole.Constants.*;

/**
 * Defines user roles
 */
public enum UserRole {
    /* Can create users with such roles as: CLIENT, CSR, PMG */
    ADMIN(ADMIN_DATABASE_ID),
    /* Post tariffs and services available in the system*/
    CSR(CSR_DATABASE_ID),
    /* Accepts complaints, responds to them */
    PMG(PMG_DATABASE_ID),
    /* Will post complaints or make requests to the system regarding
    details of the service */
    CLIENT(CLIENT_DATABASE_ID);

    private Long databaseId;

    UserRole(Long databaseId) {
        this.databaseId = databaseId;
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    static class Constants {
        static final Long ADMIN_DATABASE_ID = 1L;
        static final Long CSR_DATABASE_ID = 2L;
        static final Long PMG_DATABASE_ID = 3L;
        static final Long CLIENT_DATABASE_ID = 4L;
    }

    public String toString() {
        return this.name();
    }
}
