package com.phonecompany.model;

import com.phonecompany.model.enums.WeekOfMonth;

import java.util.EnumMap;

/**
 * Defines amount of orders distinguished by type that were made at the
 * given week of month.
 */
public final class WeeklyOrdersAmount {

    private final EnumMap<WeekOfMonth, Integer> deactivations;
    private final EnumMap<WeekOfMonth, Integer> activations;

    public WeeklyOrdersAmount(EnumMap<WeekOfMonth, Integer> deactivations,
                              EnumMap<WeekOfMonth, Integer> activations) {
        this.deactivations = deactivations;
        this.activations = activations;
    }

    public EnumMap<WeekOfMonth, Integer> getDeactivations() {
        return deactivations;
    }

    public EnumMap<WeekOfMonth, Integer> getActivations() {
        return activations;
    }

    @Override
    public String toString() {
        return "WeeklyOrdersAmount{" +
                "deactivations=" + deactivations +
                ", activations=" + activations +
                '}';
    }
}
