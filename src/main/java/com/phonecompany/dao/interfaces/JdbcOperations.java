package com.phonecompany.dao.interfaces;

import com.phonecompany.model.DomainEntity;

import java.util.List;

/**
 * Base interface for CRUD operations on entities
 *
 * @param <T> entity type that CRUD operations are performed on
 */
public interface JdbcOperations<T extends DomainEntity> {
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


    int getCountByKey(String key, String countQuery);

    List<T> executeForList(String query, Object[] params);

    T executeForObject(String query, Object[] params);

    void executeUpdate(String query, Object[] params);

    int executeForInt(String query, Object[] params);

    <E> E executeForObject(String query, Object[] params, RowMapper<E> rowMapper);
}
