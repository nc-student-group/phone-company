package com.phonecompany.model;

import com.phonecompany.model.enums.WeekOfMonth;

import java.util.EnumMap;

public class WeeklyComplaintStatistics {

    private EnumMap<WeekOfMonth, Integer> customerService;
    private EnumMap<WeekOfMonth, Integer> suggestion;
    private EnumMap<WeekOfMonth, Integer> technicalService;

    public WeeklyComplaintStatistics(EnumMap<WeekOfMonth, Integer> customerService, EnumMap<WeekOfMonth, Integer> suggestion, EnumMap<WeekOfMonth, Integer> technicalService) {
        this.customerService = customerService;
        this.suggestion = suggestion;
        this.technicalService = technicalService;
    }

    public EnumMap<WeekOfMonth, Integer> getTechnicalService() {
        return technicalService;
    }

    public void setTechnicalService(EnumMap<WeekOfMonth, Integer> technicalService) {
        this.technicalService = technicalService;
    }

    public EnumMap<WeekOfMonth, Integer> getCustomerService() {
        return customerService;
    }

    public void setCustomerService(EnumMap<WeekOfMonth, Integer> customerService) {
        this.customerService = customerService;
    }

    public EnumMap<WeekOfMonth, Integer> getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(EnumMap<WeekOfMonth, Integer> suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public String toString() {
        return "WeeklyComplaintStatistics{" +
                "customerService=" + customerService +
                ", suggestion=" + suggestion +
                ", technicalService=" + technicalService +
                '}';
    }
}
