package com.phonecompany.service;

import com.phonecompany.annotations.CacheClear;
import com.phonecompany.annotations.Cacheable;
import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.DomainEntity;
import com.phonecompany.service.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CrudServiceImpl<T extends DomainEntity>
        implements CrudService<T> {

    @Autowired
    protected CrudDao<T> dao;

    public CrudServiceImpl(CrudDao<T> dao) {
        this.dao = dao;
    }

    public CrudServiceImpl() {
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
    public List<T> getAll() {
        return dao.getAll();
    }

}
