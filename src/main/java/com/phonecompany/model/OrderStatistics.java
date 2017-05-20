package com.phonecompany.model;

import com.phonecompany.model.enums.ItemType;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.xssfHelper.Statistics;

import java.time.LocalDate;

public final class OrderStatistics implements Statistics {

    private final long count;
    private final String targetName;
    private final OrderType orderType;
    private final LocalDate creationDate;

    public OrderStatistics(long count, String targetName,
                           OrderType orderType, LocalDate creationDate) {
        this.count = count;
        this.targetName = targetName;
        this.orderType = orderType;
        this.creationDate = creationDate;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public String getItemName() {
        return targetName;
    }

    @Override
    public ItemType getItemType() {
        return orderType;
    }

    @Override
    public LocalDate getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "OrderStatistics{" +
                "count=" + count +
                ", targetName='" + targetName + '\'' +
                ", orderType=" + orderType +
                ", creationDate=" + creationDate +
                '}';
    }
}
