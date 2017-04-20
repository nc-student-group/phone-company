package com.phonecompany.model.enums;

import static com.phonecompany.model.enums.CustomerCategory.Constants.BUSINESS_CATEGORY_DATABASE_ID;
import static com.phonecompany.model.enums.CustomerCategory.Constants.RESIDENTIAL_CATEGORY_DATABASE_ID;

public enum CustomerCategory {

    RESIDENTIAL_CUSTOMER(RESIDENTIAL_CATEGORY_DATABASE_ID),
    BUSINESS_CUSTOMER(BUSINESS_CATEGORY_DATABASE_ID);

    private long categoryDatabaseId;

    CustomerCategory(long categoryDatabaseId) {
        this.categoryDatabaseId = categoryDatabaseId;
    }

    public long getCategoryDatabaseId() {
        return categoryDatabaseId;
    }

    static class Constants {
        static final int RESIDENTIAL_CATEGORY_DATABASE_ID = 1;
        static final int BUSINESS_CATEGORY_DATABASE_ID = 2;
    }
}
