package com.phonecompany.service.interfaces;

import java.util.List;

/**
 * Base interface for service operations on DAO
 *
 * @param <T> entity type that DAO implements CRUD operation for
 */
public interface CrudService<T> {

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
     * Gets all the entities
     *
     * @return all entities
     */
    List<T> getAll();

    void beginTransaction();
    void commit();
    void rollback();
    public void setAutoCommit(boolean autoCommit);
}

