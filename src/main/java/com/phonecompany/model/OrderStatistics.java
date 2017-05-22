package com.phonecompany.model;

import com.phonecompany.model.enums.interfaces.ItemType;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.interfaces.Statistics;

import java.time.LocalDate;

/**
 * {@inheritDoc}
 */
public final class OrderStatistics implements Statistics {

    private final long count;
    private final String productName;
    private final OrderType orderType;
    private final LocalDate creationDate;

    public OrderStatistics(long count, String productName,
                           OrderType orderType, LocalDate creationDate) {
        this.count = count;
        this.productName = productName;
        this.orderType = orderType;
        this.creationDate = creationDate;
    }

    @Override
    public long getValue() {
        return count;
    }

    @Override
    public String getItemName() {
        return productName;
    }

    @Override
    public ItemType getItemType() {
        return orderType;
    }

    @Override
    public LocalDate getTimePoint() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "OrderStatistics{" +
                "count=" + count +
                ", productName='" + productName + '\'' +
                ", orderType=" + orderType +
                ", creationDate=" + creationDate +
                '}';
    }
}
