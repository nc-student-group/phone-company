package com.phonecompany.model.enums;

public enum WeekOfMonth {
    FIRST_WEEK,
    SECOND_WEEK,
    THIRD_WEEK,
    FOURTH_WEEK;

    public WeekOfMonth next() {
        return values()[(ordinal() + 1) % values().length];
    }
}
