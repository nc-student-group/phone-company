package com.phonecompany.dao.interfaces;

import com.phonecompany.model.DomainEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AbstractPageableDao<T extends DomainEntity>
        extends CrudDao<T> {
    int getEntityCount(Object... args);
    List<T> getPaging(int page, int size, Object... args);
}
