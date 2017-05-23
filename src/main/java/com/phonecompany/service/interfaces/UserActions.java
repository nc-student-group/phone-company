package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.events.OnUserCreationEvent;

import java.util.List;
import java.util.Map;

public interface UserActions extends CrudService<User>, AbstractUserService<User> {
    User resetPassword(User user);

    Map<String, Object> getAllUsersPaging(int page, int size, int role, String status, String email, int orderBy,
                                          String orderByType);

    User getCurrentlyLoggedInUser();

    void changePassword(String oldPass, String newPass, User user);
}
