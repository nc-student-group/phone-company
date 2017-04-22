package com.phonecompany.model;

/**
 * Gets emitted on reset password request
 *
 * @see com.phonecompany.service.CustomerServiceImpl
 */
public final class ResetPasswordEvent<T> {

    private final T userToReset;

    public ResetPasswordEvent(T userToReset) {
        this.userToReset = userToReset;
    }

    public T getUserToReset() {
        return userToReset;
    }

    @Override
    public String toString() {
        return "ResetPasswordEvent{" +
                "userToReset=" + userToReset +
                '}';
    }
}
