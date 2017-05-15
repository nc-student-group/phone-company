package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;

import java.util.List;

public interface UserService extends CrudService<User>,
        AbstractUserService<User>, UserActions {
    List<User> getAllUsersSearch(int page,int size,String email,int userRole,String status);
    int  getCountSearch(int page,int size,String email,int userRole,String status);
}
