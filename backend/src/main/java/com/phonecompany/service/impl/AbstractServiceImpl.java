package com.phonecompany.service.impl;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.DomainEntity;
import com.phonecompany.service.interfaces.AbstractService;

import java.util.List;

/**
 * Created by Yurii on 14.04.2017.
 */
public class AbstractServiceImpl<T extends DomainEntity> implements AbstractService<T> {

    private CrudDao<T> crudDao;
    public AbstractServiceImpl(CrudDao<T> dao) {
        super();
        this.crudDao = dao;
    }

    @Override
    public T save(T entity) {
        return crudDao.save(entity);
    }

    @Override
    public T update(T entity) {
        return crudDao.update(entity);
    }

    @Override
    public T getById(Long id) {
        return crudDao.getById(id);
    }

    @Override
    public void delete(Long id) {
        crudDao.delete(id);
    }

    @Override
    public List<T> getAll() {
        return crudDao.getAll();
    }
}
