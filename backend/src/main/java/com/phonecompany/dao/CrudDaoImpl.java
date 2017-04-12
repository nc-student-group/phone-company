package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CrudDao;

import java.util.List;

public abstract class CrudDaoImpl<T> implements CrudDao<T> {

    @Override
    public T save(T entity) {
        return null;
    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public T getById(Long id) {
        return null;
    }

    @Override
    public List<T> getAll() {
        return null;
    }
}
