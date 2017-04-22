package com.phonecompany.model;

/**
 * Gets emitted when user finishes his registration
 *
 * @see com.phonecompany.service.UserServiceImpl
 */
public final class OnRegistrationCompleteEvent<T> {

    private final T persistedUser;

    public OnRegistrationCompleteEvent(T persistedUser) {
        this.persistedUser = persistedUser;
    }

    public T getPersistedUser() {
        return persistedUser;
    }

    @Override
    public String toString() {
        return "OnRegistrationCompleteEvent{" +
                "persistedUser=" + persistedUser +
                '}';
    }
}
