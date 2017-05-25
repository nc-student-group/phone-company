package com.phonecompany.exception.service_layer;

import java.util.List;

/**
 * Gets thrown if target was not properly filtered.
 */
public class InsufficientFiltrationException extends IllegalStateException {

    public InsufficientFiltrationException(List<?> failedList) {
        this("There should be only one element in the list at the end of the filter chain. " +
                "But actual number is: " + failedList.size());
    }

    public InsufficientFiltrationException(String message) {
        super(message);
    }
}
