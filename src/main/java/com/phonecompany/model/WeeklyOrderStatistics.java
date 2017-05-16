package com.phonecompany.model;

import com.phonecompany.model.enums.WeekOfMonth;

import java.util.EnumMap;

public class WeeklyOrderStatistics {

    private EnumMap<WeekOfMonth, Integer> deactivations;
    private EnumMap<WeekOfMonth, Integer> activations;

    public WeeklyOrderStatistics(EnumMap<WeekOfMonth, Integer> deactivations,
                                 EnumMap<WeekOfMonth, Integer> activations) {
        this.deactivations = deactivations;
        this.activations = activations;
    }

    public EnumMap<WeekOfMonth, Integer> getDeactivations() {
        return deactivations;
    }

    public void setDeactivations(EnumMap<WeekOfMonth, Integer> deactivations) {
        this.deactivations = deactivations;
    }

    public EnumMap<WeekOfMonth, Integer> getActivations() {
        return activations;
    }

    public void setActivations(EnumMap<WeekOfMonth, Integer> activations) {
        this.activations = activations;
    }

    @Override
    public String toString() {
        return "WeeklyOrderStatistics{" +
                "deactivations=" + deactivations +
                ", activations=" + activations +
                '}';
    }
}
