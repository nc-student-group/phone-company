package com.phonecompany.model;

public final class OnRegistrationCompleteEvent {

    private final User persistedUser;

    public OnRegistrationCompleteEvent(User persistedUser) {
        this.persistedUser = persistedUser;
    }

    public User getPersistedUser() {
        return persistedUser;
    }
}
