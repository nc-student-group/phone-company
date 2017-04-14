package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.RoleDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Role;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Yurii on 14.04.2017.
 */
@Repository
public class RoleDaoImpl extends CrudDaoImpl<Role> implements RoleDao{

    private QueryLoader queryLoader;

    @Autowired
    public RoleDaoImpl(QueryLoader queryLoader){
        this.queryLoader = queryLoader;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.role." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Role role) {
        try {
            preparedStatement.setString(1, role.getName());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Role init(ResultSet resultSet) {
        Role role = new Role();
        try {
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return role;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Role role) {
        try {
            preparedStatement.setString(1, role.getName());
            preparedStatement.setLong(2, role.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }
}
