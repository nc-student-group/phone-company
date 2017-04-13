package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.DomainEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.validation.Valid;
import java.sql.*;
import java.util.ArrayList;
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
        Connection conn = DriverManager.getConnection(connStr);
        PreparedStatement preparedStatement = conn.prepareStatement(getQuery("insert"));
        getParams(entity).forEach((Integer s, Object o) -> {
            try {
                preparedStatement.setObject(s, o);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        setId(entity, rs.getLong(1));
        preparedStatement.close();
        conn.close();
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getById(Long id) throws SQLException {
        Connection conn = DriverManager.getConnection(connStr);
        PreparedStatement preparedStatement = conn.prepareStatement(getQuery("getById"));
        preparedStatement.setLong(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        T res = init(rs);
        preparedStatement.close();
        conn.close();
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws SQLException {
        Connection conn = DriverManager.getConnection(connStr);

        PreparedStatement preparedStatement = conn.prepareStatement(getQuery("delete"));
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();

        preparedStatement.close();
        conn.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() throws SQLException {
        Connection conn = DriverManager.getConnection(connStr);
        PreparedStatement preparedStatement = conn.prepareStatement(getQuery("getAll"));
        ResultSet rs = preparedStatement.executeQuery();
        List<T> result = new ArrayList<>();
        while(rs.next()) {
            result.add(init(rs));
        }
        preparedStatement.close();
        conn.close();
        return result;
    }

    public abstract String getQuery(String type);

    public abstract Map<Integer, Object> getParams(T o);

    public abstract void setId(T obj, long id);

    public abstract T init(ResultSet resultSet);
}
