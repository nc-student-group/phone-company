package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AbstractPageableDao;
import com.phonecompany.model.DomainEntity;

import java.util.List;

public abstract class AbstractPageableDaoImpl<T extends DomainEntity>
        extends CrudDaoImpl<T> implements AbstractPageableDao<T> {

    @Override
    public List<T> getByIdAndPaging(long entityId, int page, int size) {
        return null;
    }
}
