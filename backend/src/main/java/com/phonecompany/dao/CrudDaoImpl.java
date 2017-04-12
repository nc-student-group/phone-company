package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CrudDao;

import java.util.List;

public class CrudDaoImpl<T> implements CrudDao<T> {

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
