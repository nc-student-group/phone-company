package com.phonecompany.service.interfaces;

import com.phonecompany.model.enums.interfaces.ItemType;

import java.time.LocalDate;

/**
 * Class that represents statistical information regarding some type
 * contained in the system.
 */
public interface Statistics {

    long getValue();           //e.g. number of orders

    LocalDate getTimePoint();  //e.g. date at which those orders were made

    String getItemName();

    ItemType getItemType();
}
