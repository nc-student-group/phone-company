package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;
import com.phonecompany.model.events.ResetPasswordEvent;

public interface AbstractUserService<T extends User> {
    T resetPassword(ResetPasswordEvent<T> resetPasswordEvent);
    T findByEmail(String email);
}
