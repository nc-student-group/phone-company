package com.phonecompany.model;


public class OnUserCreationEvent {
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
