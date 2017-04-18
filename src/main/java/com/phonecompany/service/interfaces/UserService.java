package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;

public interface UserService extends CrudService<User> {
    User findByEmail(String email);
    User resetPassword(User user);
}
