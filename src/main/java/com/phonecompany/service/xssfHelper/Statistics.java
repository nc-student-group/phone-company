package com.phonecompany.service.xssfHelper;

import com.phonecompany.model.enums.ItemType;

import java.time.LocalDate;

public interface Statistics {

    long getCount();

    String getItemName();

    ItemType getItemType();

    LocalDate getCreationDate();
}
