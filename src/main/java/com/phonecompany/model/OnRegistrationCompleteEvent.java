package com.phonecompany.model;

/**
 * Gets emitted when user finishes his registration
 *
 * @see com.phonecompany.service.UserServiceImpl
 */
public final class OnRegistrationCompleteEvent {

    private final User persistedUser;

    public OnRegistrationCompleteEvent(User persistedUser) {
        this.persistedUser = persistedUser;
    }

    public User getPersistedUser() {
        return persistedUser;
    }

    @Override
    public String toString() {
        return "OnRegistrationCompleteEvent{" +
                "persistedUser=" + persistedUser +
                '}';
    }
}
