package com.phonecompany.model;

import com.phonecompany.model.enums.OrderType;

import java.time.LocalDate;

public final class Statistics {

    private final long count;
    private final String targetName;
    private final LocalDate creationDate;
    private final OrderType orderType;

    public Statistics(long count, String targetName,
                      LocalDate creationDate, OrderType orderType) {
        this.count = count;
        this.targetName = targetName;
        this.creationDate = creationDate;
        this.orderType = orderType;
    }

    public long getCount() {
        return count;
    }

    public String getTargetName() {
        return targetName;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "count=" + count +
                ", targetName='" + targetName + '\'' +
                ", creationDate=" + creationDate +
                ", orderType=" + orderType +
                '}';
    }
}
