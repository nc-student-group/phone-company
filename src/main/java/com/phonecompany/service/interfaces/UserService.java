package com.phonecompany.service.interfaces;

import com.phonecompany.model.Customer;
import com.phonecompany.model.User;

import java.util.List;

public interface UserService extends CrudService<User>, AbstractUserService<User> {
    User resetPassword(User user);
    public List<User> getAllUsersPaging(int page, int size, int role, String status);
    public int getCountUsers(int role,String status);
}
