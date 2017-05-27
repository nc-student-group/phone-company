package com.phonecompany.model.enums;

import com.phonecompany.model.enums.interfaces.Storable;

import static com.phonecompany.model.enums.WeekOfMonth.Constants.*;

/**
 * Defines weeks of month.
 *
 * @see com.phonecompany.model.WeeklyComplaintsAmount
 * @see com.phonecompany.model.WeeklyOrdersAmount
 */
public enum WeekOfMonth implements Storable {
    FIRST_WEEK(FIRST_WEEK_DATABASE_ID),
    SECOND_WEEK(SECOND_WEEK_DATABASE_ID),
    THIRD_WEEK(THIRD_WEEK_DATABASE_ID),
    FOURTH_WEEK(FOURTH_WEEK_DATABASE_ID);

    private Long databaseId;

    WeekOfMonth(Long databaseId) {
        this.databaseId = databaseId;
    }

    @Override
    public Long getId() {
        return databaseId;
    }

    /**
     * Constants that define correspondence between {@code Enum} objects
     * declared by the outer class and their IDs from the storage.
     */
    static class Constants {
        static final Long FIRST_WEEK_DATABASE_ID = 1L;
        static final Long SECOND_WEEK_DATABASE_ID = 2L;
        static final Long THIRD_WEEK_DATABASE_ID = 3L;
        static final Long FOURTH_WEEK_DATABASE_ID = 4L;
    }
}
