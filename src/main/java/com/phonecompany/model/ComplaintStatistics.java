package com.phonecompany.model;

import com.phonecompany.model.enums.ItemType;
import com.phonecompany.service.xssfHelper.Statistics;

import java.time.LocalDate;

public final class ComplaintStatistics implements Statistics {

    private final long count;
    private final String itemName;
    private final ItemType itemType;
    private final LocalDate creationDate;

    public ComplaintStatistics(long count, String itemName,
                               ItemType itemType, LocalDate creationDate) {
        this.count = count;
        this.itemName = itemName;
        this.itemType = itemType;
        this.creationDate = creationDate;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public LocalDate getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "OrderStatistics{" +
                "count=" + count +
                ", itemName='" + itemName + '\'' +
                ", itemType=" + itemType +
                ", creationDate=" + creationDate +
                '}';
    }
}
