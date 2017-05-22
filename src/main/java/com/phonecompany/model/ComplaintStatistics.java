package com.phonecompany.model;

import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.model.enums.interfaces.ItemType;
import com.phonecompany.service.interfaces.Statistics;

import java.time.LocalDate;

/**
 * {@inheritDoc}
 */
public final class ComplaintStatistics implements Statistics {

    private final long count;
    private final String itemName;
    private final ComplaintStatus complaintStatus;
    private final LocalDate creationDate;

    public ComplaintStatistics(long count, String itemName,
                               ComplaintStatus complaintStatus, LocalDate creationDate) {
        this.count = count;
        this.itemName = itemName;
        this.complaintStatus = complaintStatus;
        this.creationDate = creationDate;
    }

    @Override
    public long getValue() {
        return count;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public ItemType getItemType() {
        return complaintStatus;
    }

    @Override
    public LocalDate getTimePoint() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "OrderStatistics{" +
                "count=" + count +
                ", itemName='" + itemName + '\'' +
                ", complaintStatus=" + complaintStatus +
                ", creationDate=" + creationDate +
                '}';
    }
}
