package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.events.ResetPasswordEvent;

public interface AbstractUserService<T extends User>
        extends CrudService<T> {

    T getCurrentlyLoggedInUser();

    T findByEmail(String email);

    void updateStatus(long id, Status status);

}
