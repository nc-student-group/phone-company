package com.phonecompany.model.enums;

import com.phonecompany.model.enums.interfaces.DataBaseEnum;

import static com.phonecompany.model.enums.WeekOfMonth.Constants.*;

/**
 * Defines weeks of month
 *
 * @see
 */
public enum WeekOfMonth implements DataBaseEnum {
    FIRST_WEEK(FIRST_WEEK_DATABASE_ID),
    SECOND_WEEK(SECOND_WEEK_DATABASE_ID),
    THIRD_WEEK(THIRD_WEEK_DATABASE_ID),
    FOURTH_WEEK(FOURTH_WEEK_DATABASE_ID);

    private Long databaseId;

    WeekOfMonth(Long databaseId) {
        this.databaseId = databaseId;
    }

    @Override
    public Long getDatabaseId() {
        return databaseId;
    }

    static class Constants {
        static final Long FIRST_WEEK_DATABASE_ID = 1L;
        static final Long SECOND_WEEK_DATABASE_ID = 2L;
        static final Long THIRD_WEEK_DATABASE_ID = 3L;
        static final Long FOURTH_WEEK_DATABASE_ID = 4L;
    }
}
