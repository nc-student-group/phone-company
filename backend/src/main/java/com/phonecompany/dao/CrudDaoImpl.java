package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.model.DomainEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.List;
import java.util.Map;

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
    public T save(T entity) throws SQLException {
        try (Connection conn = DriverManager.getConnection(connStr);
             PreparedStatement preparedStatement = conn.prepareStatement(getQuery("insert"))) {
                this.getParams(entity).forEach((Integer index, Object parameter) -> {
                    try {
                        preparedStatement.setObject(index, parameter);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
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
    public T getById(Long id) {
        try (Connection conn = DriverManager.getConnection(connStr);
             PreparedStatement ps = conn.prepareStatement(getQuery("getById"))) {
            ps.setObject(1, id);
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
    public void delete(Long id) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() {
        return null;
    }

    public abstract String getQuery(String type);
    public abstract Map<Integer, Object> getParams(T o);
    public abstract void setId(T o, long id);
    public abstract T init(ResultSet resultSet);
}
