package com.phonecompany.service.interfaces;

import com.phonecompany.model.DomainEntity;

import java.util.List;

/**
 * Created by Yurii on 14.04.2017.
 */
public interface AbstractService<T extends DomainEntity> {
    public T save(T entity);

    public T update(T entity);

    public T getById(Long id);

    public void delete(Long id);

    public List<T> getAll();
}
