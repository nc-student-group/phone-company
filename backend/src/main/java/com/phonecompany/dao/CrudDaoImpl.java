package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.DomainEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@NoArgsConstructor
public abstract class CrudDaoImpl<T extends DomainEntity>
        implements CrudDao<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public T save(T entity) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() {
        return null;
    }
}
