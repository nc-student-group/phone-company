package com.phonecompany.dao.interfaces;

import java.util.List;

public interface CrudDao<T> {
    T save(T entity);
    void delete(T entity);
    T getById(Long id);
    List<T> getAll();
}
