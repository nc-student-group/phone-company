package com.phonecompany.dao.interfaces;

import com.phonecompany.model.DomainEntity;

import java.util.List;

public interface AbstractPageableDao<T extends DomainEntity>
        extends CrudDao<T> {
    List<T> getByIdAndPaging(long entityId, int page, int size);
}
