package com.phonecompany.model;

import java.util.List;

public class ComplaintStatisticsDTO {

    private List<List<Complaint>> customerService;
    private List<List<Complaint>> suggestion;
    private List<List<Complaint>> technicalService;

    public ComplaintStatisticsDTO(List<List<Complaint>> customerService, List<List<Complaint>> suggestion, List<List<Complaint>> technicalService) {
        this.customerService = customerService;
        this.suggestion = suggestion;
        this.technicalService = technicalService;
    }

    public List<List<Complaint>> getCustomerService() {
        return customerService;
    }

    public void setCustomerService(List<List<Complaint>> customerService) {
        this.customerService = customerService;
    }

    public List<List<Complaint>> getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(List<List<Complaint>> suggestion) {
        this.suggestion = suggestion;
    }

    public List<List<Complaint>> getTechnicalService() {
        return technicalService;
    }

    public void setTechnicalService(List<List<Complaint>> technicalService) {
        this.technicalService = technicalService;
    }

    @Override
    public String toString() {
        return "ComplaintStatisticsDTO{" +
                "customerService=" + customerService +
                ", suggestion=" + suggestion +
                ", technicalService=" + technicalService +
                '}';
    }
}
