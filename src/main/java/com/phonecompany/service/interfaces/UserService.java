package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;

import java.util.List;
import java.util.Map;

public interface UserService extends CrudService<User>,
        AbstractUserService<User>, UserActions {
    Map<String, Object> getAllUsersSearch(int page, int size, String email, int userRole, String status);
}
