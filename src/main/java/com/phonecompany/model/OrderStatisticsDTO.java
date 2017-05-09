package com.phonecompany.model;

import java.util.List;

public class OrderStatisticsDTO {

    private List<List<Order>> deactivations;
    private List<List<Order>> activations;

    public OrderStatisticsDTO(List<List<Order>> deactivations,
                              List<List<Order>> activations) {
        this.deactivations = deactivations;
        this.activations = activations;
    }

    public List<List<Order>> getDeactivations() {
        return deactivations;
    }

    public void setDeactivations(List<List<Order>> deactivations) {
        this.deactivations = deactivations;
    }

    public List<List<Order>> getActivations() {
        return activations;
    }

    public void setActivations(List<List<Order>> activations) {
        this.activations = activations;
    }

    @Override
    public String toString() {
        return "OrderStatisticsDTO{" +
                "deactivations=" + deactivations +
                ", activations=" + activations +
                '}';
    }
}
