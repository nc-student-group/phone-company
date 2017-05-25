package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;

import java.util.Map;

public interface UserActions extends CrudService<User>, AbstractUserService<User> {
    String resetPassword(User user);

    Map<String, Object> getAllUsersPaging(int page, int size, int role, String status, String email, int orderBy,
                                          String orderByType);

    User getCurrentlyLoggedInUser();

    void changePassword(String oldPass, String newPass, User user);
}
