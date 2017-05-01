package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;

import java.util.List;

public interface UserService extends CrudService<User>, AbstractUserService<User> {
    User resetPassword(User user);
    List<User> getAllUsersPaging(int page, int size, int role, String status);
    int getCountUsers(int role,String status);
    User getCurrentlyLoggedInUser();
}
