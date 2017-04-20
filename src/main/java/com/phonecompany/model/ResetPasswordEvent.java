package com.phonecompany.model;

/**
 * Gets emitted on user's reset password request
 *
 * @see com.phonecompany.service.UserServiceImpl
 */
public final class ResetPasswordEvent {

    private final User userToReset;

    public ResetPasswordEvent(User userToReset) {
        this.userToReset = userToReset;
    }

    public User getUserToReset() {
        return userToReset;
    }

    @Override
    public String toString() {
        return "ResetPasswordEvent{" +
                "userToReset=" + userToReset +
                '}';
    }
}
