package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AbstractUserDao;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.model.DomainEntity;
import com.phonecompany.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractUserDaoImpl<T extends User>
    extends CrudDaoImpl<T> implements AbstractUserDao<T> {

    @Override
    public T findByEmail(String email) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getByEmail"))) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return init(rs);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(email, e);
        }
        return null;
    }
}
