package com.phonecompany.dao.interfaces;

import com.phonecompany.model.DomainEntity;

public interface AbstractUserDao<T extends DomainEntity>
        extends CrudDao<T> {
    T findByEmail(String email);
}
