package com.phonecompany.exception.service_layer;

import java.util.List;

/**
 *
 */
public class InsufficientFilteringException extends RuntimeException {

    public InsufficientFilteringException(List<?> failedList) {
        this("There should be only one element in the list at the end of the filter chain. " +
                "But actual number is: " + failedList.size());
    }

    public InsufficientFilteringException(String message) {
        super(message);
    }
}
