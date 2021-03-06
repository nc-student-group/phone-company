package com.phonecompany.service;

import com.phonecompany.dao.interfaces.JdbcOperations;
import com.phonecompany.model.DomainEntity;
import com.phonecompany.service.interfaces.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CrudServiceImpl<T extends DomainEntity>
        implements CrudService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CrudServiceImpl.class);

    @Autowired
    protected JdbcOperations<T> dao;

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
