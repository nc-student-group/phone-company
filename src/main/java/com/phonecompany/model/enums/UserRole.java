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

    private long databaseId;

    UserRole(long databaseId) {
        this.databaseId = databaseId;
    }

    public long getDatabaseId() {
        return databaseId;
    }

    static class Constants {
        static final long ADMIN_DATABASE_ID = 1;
        static final long CSR_DATABASE_ID = 2;
        static final long PMG_DATABASE_ID = 3;
        static final long CLIENT_DATABASE_ID = 4;
    }

    public String toString() {
        return this.name();
    }
}
