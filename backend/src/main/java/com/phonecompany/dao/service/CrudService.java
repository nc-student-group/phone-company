package com.phonecompany.dao.service;

import com.phonecompany.dao.interfaces.CrudDao;

import java.util.List;

/**
 * Base interface for service operations on DAO
 *
 * @param <K> DAO type that service operations are performed on
 * @param <T> entity type that DAO implements CRUD operation for
 */
public interface CrudService<T, K extends CrudDao<T>> {

    /**
     * Saves entity
     *
     * @param entity entity to be saved
     * @return saved entity
     */
    T save(T entity);

    /**
     * Updates entity
     *
     * @param entity entity to be saved
     * @return saved entity
     */
    T update(T entity);

    /**
     * Gets entity by its id
     *
     * @param id entity identifier
     * @return entity found by the provided identifier
     */
    T getById(Long id);

    /**
     * Deletes entity by the given id
     *
     * @param id identifier of the entity that has to be deleted
     */
    void delete(Long id);

    /**
     * Gets all the entities
     *
     * @return all entities
     */
    List<T> getAll();
}

