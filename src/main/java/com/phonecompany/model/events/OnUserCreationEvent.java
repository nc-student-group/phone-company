package com.phonecompany.model.events;

import com.phonecompany.model.User;

/**
 * Gets emitted when user gets created
 *
 * @see com.phonecompany.controller.UserController
 */
public final class OnUserCreationEvent {

    private final User persistedUser;

    public OnUserCreationEvent(User persistedUser) {
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
