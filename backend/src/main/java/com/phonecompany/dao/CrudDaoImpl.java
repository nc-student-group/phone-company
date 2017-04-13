package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.DomainEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.validation.Valid;
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
        preparedStatement.setObject(1, id);
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
    public void delete(Long id) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() {
        return null;
    }

    public String getQuery(String type) {
        return "";
    }

    public Map<Integer, Object> getParams(Object o) {
        return null;
    }

    public void setId(Object o, long id) {
    }

    public T init(ResultSet resultSet){
        return null;
    }
}
