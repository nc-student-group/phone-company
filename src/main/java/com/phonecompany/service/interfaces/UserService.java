package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;

public interface UserService extends CrudService<User>, AbstractUserService<User> {
    User resetPassword(User user);
}
