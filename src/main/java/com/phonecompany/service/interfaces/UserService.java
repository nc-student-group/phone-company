package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;

public interface UserService extends CrudService<User> {
    User findByUsername(String userName);
    User resetPassword(User user);
}
