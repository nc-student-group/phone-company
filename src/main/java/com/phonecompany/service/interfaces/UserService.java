package com.phonecompany.service.interfaces;

import com.phonecompany.model.OnRegistrationCompleteEvent;
import com.phonecompany.model.ResetPasswordEvent;
import com.phonecompany.model.User;

public interface UserService extends CrudService<User> {
    User findByUsername(String userName);
    User resetPassword(ResetPasswordEvent resetPasswordEvent);
    void confirmRegistration(OnRegistrationCompleteEvent registrationCompleteEvent);
    User findByEmail(String email);
    String encryptPassword(String password);
}
