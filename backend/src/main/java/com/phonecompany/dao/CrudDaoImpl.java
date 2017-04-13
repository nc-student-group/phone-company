package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.model.DomainEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
public abstract class CrudDaoImpl<T extends DomainEntity>
        implements CrudDao<T> {

    @Value("${spring.datasource.url}")
    private String connStr;

    /**
     * {@inheritDoc}
     */
    @Override
    public T insert(T entity) throws SQLException {
        try(Connection conn = DriverManager.getConnection(connStr);
            PreparedStatement preparedStatement = conn.prepareStatement(getQuery("insert"))) {
            this.setParamsForSaveStatement(entity, preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            setId(entity, rs.getLong(1));
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(T entity) throws SQLException {
        try(Connection conn = DriverManager.getConnection(connStr);
            PreparedStatement preparedStatement = conn.prepareStatement(getQuery("update"))) {
            this.setParamsForSaveStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getById(Long id) {
        try (Connection conn = DriverManager.getConnection(connStr);
             PreparedStatement ps = conn.prepareStatement(getQuery("getById"))) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return init(rs);
        } catch (SQLException e) {
            throw new EntityNotFoundException(id, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws SQLException {
        try(Connection conn = DriverManager.getConnection(connStr);
            PreparedStatement preparedStatement = conn.prepareStatement(getQuery("delete"))) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() throws SQLException {
        try(Connection conn = DriverManager.getConnection(connStr);
            PreparedStatement preparedStatement = conn.prepareStatement(getQuery("getAll"))) {
            ResultSet rs = preparedStatement.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        }

    }

    public abstract String getQuery(String type);

    public abstract void setParamsForSaveStatement(T o, PreparedStatement preparedStatement);

    public abstract void setId(T obj, long id);

    public abstract T init(ResultSet resultSet);
}
