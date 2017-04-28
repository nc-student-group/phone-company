package com.phonecompany.dao.interfaces;

import com.phonecompany.model.DomainEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AbstractPageableDao<T extends DomainEntity>
        extends CrudDao<T> {
    List<T> getPagingByParametersMap(int page, int size, Map<String, Object> params);
    int getEntityCount(Map<String, Object> params);
}
