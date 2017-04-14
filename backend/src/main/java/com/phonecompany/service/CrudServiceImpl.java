package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.DomainEntity;
import com.phonecompany.service.interfaces.CrudService;

import java.util.List;

public abstract class CrudServiceImpl<T extends DomainEntity> implements CrudService<T> {

    private CrudDao<T> dao;

    public CrudServiceImpl(CrudDao<T> dao) {
        this.dao = dao;
    }

    @Override
    public T save(T entity) {
        return dao.save(entity);
    }

    @Override
    public T update(T entity) {
        return dao.update(entity);
    }

    @Override
    public T getById(Long id) {
        return dao.getById(id);
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }

    @Override
    public List<T> getAll() {
        return dao.getAll();
    }
}
