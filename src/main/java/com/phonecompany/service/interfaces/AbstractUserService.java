package com.phonecompany.service.interfaces;

import com.phonecompany.model.ResetPasswordEvent;

public interface AbstractUserService<T> {
    T resetPassword(ResetPasswordEvent<T> resetPasswordEvent);
    T findByEmail(String email);

}
