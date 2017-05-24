package com.phonecompany.model;

import com.phonecompany.model.enums.WeekOfMonth;

import java.util.EnumMap;

/**
 * Defines amount of orders distinguished by type that were made at the
 * given week of month.
 */
public final class WeeklyOrdersAmount {

    private final EnumMap<WeekOfMonth, Integer> serviceActivations;
    private final EnumMap<WeekOfMonth, Integer> serviceDeactivations;
    private final EnumMap<WeekOfMonth, Integer> tariffActivations;
    private final EnumMap<WeekOfMonth, Integer> tariffDeactivations;

    public WeeklyOrdersAmount(EnumMap<WeekOfMonth, Integer> serviceActivations,
                              EnumMap<WeekOfMonth, Integer> serviceDeactivations,
                              EnumMap<WeekOfMonth, Integer> tariffActivations,
                              EnumMap<WeekOfMonth, Integer> tariffDeactivations) {
        this.serviceActivations = serviceActivations;
        this.serviceDeactivations = serviceDeactivations;
        this.tariffActivations = tariffActivations;
        this.tariffDeactivations = tariffDeactivations;
    }

    public EnumMap<WeekOfMonth, Integer> getServiceActivations() {
        return serviceActivations;
    }

    public EnumMap<WeekOfMonth, Integer> getServiceDeactivations() {
        return serviceDeactivations;
    }

    public EnumMap<WeekOfMonth, Integer> getTariffActivations() {
        return tariffActivations;
    }

    public EnumMap<WeekOfMonth, Integer> getTariffDeactivations() {
        return tariffDeactivations;
    }

    @Override
    public String toString() {
        return "WeeklyOrdersAmount{" +
                "serviceActivations=" + serviceActivations +
                ", serviceDeactivations=" + serviceDeactivations +
                ", tariffActivations=" + tariffActivations +
                ", tariffDeactivations=" + tariffDeactivations +
                '}';
    }
}
