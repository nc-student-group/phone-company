package com.phonecompany.dao.interfaces;

import com.phonecompany.model.DomainEntity;
import com.phonecompany.model.enums.Status;

public interface AbstractUserDao<T extends DomainEntity>
        extends CrudDao<T> {
    T findByEmail(String email);

    void updateStatus(long id, Status status);

    int getCountByEmail(String email);
}
