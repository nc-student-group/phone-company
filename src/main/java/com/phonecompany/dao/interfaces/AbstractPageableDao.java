package com.phonecompany.dao.interfaces;

import com.phonecompany.model.DomainEntity;

import java.util.List;

public interface AbstractPageableDao<T extends DomainEntity>
        extends JdbcOperations<T> {
    int getEntityCount(Object... args);
    List<T> getPaging(int page, int size, Object... args);
}
