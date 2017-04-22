package com.phonecompany.dao;

import com.phonecompany.dao.CrudDaoImpl;
import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Address;
import com.phonecompany.model.Complaint;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ComplaintsDaoImpl extends CrudDaoImpl<Complaint> implements ComplaintDao {

    private QueryLoader queryLoader;
    private UserDao userDao;

    @Autowired
    public ComplaintsDaoImpl(QueryLoader queryLoader, UserDao userDao){
        this.queryLoader = queryLoader;
        this.userDao = userDao;
    }
    @Override
    public String getQuery(String type) {
            return queryLoader.getQuery("query.complaint."+type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Complaint entity) {
        try {
            preparedStatement.setString(1, entity.getStatus());
            preparedStatement.setDate(2, entity.getDate());
            preparedStatement.setString(3, entity.getText());
            preparedStatement.setString(4, entity.getType());
            preparedStatement.setLong(5, TypeMapper.getNullableId(entity.getUser()));
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Complaint entity) {
        try {
            preparedStatement.setString(1, entity.getStatus());
            preparedStatement.setDate(2, entity.getDate());
            preparedStatement.setString(3, entity.getText());
            preparedStatement.setString(4, entity.getType());
            preparedStatement.setLong(5, TypeMapper.getNullableId(entity.getUser()));

            preparedStatement.setLong(6, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Complaint init(ResultSet rs) {
        Complaint complaint = new Complaint();
        try {
            complaint.setId(rs.getLong("id"));
            complaint.setStatus(rs.getString("status"));
            complaint.setDate(rs.getDate("date"));
            complaint.setText(rs.getString("text"));
            complaint.setType(rs.getString("type"));
            complaint.setUser(userDao.getById(rs.getLong("user_id")));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return complaint;
    }
}
