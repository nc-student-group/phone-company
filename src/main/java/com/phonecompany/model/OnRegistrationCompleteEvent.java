package com.phonecompany.model;

/**
 * Gets emitted when user finishes his registration
 *
 * @see com.phonecompany.service.CustomerServiceImpl
 */
public final class OnRegistrationCompleteEvent {

    private final Customer persistedUser;

    public OnRegistrationCompleteEvent(Customer persistedUser) {
        this.persistedUser = persistedUser;
    }

    public Customer getPersistedUser() {
        return persistedUser;
    }

    @Override
    public String toString() {
        return "OnRegistrationCompleteEvent{" +
                "persistedUser=" + persistedUser +
                '}';
    }
}
