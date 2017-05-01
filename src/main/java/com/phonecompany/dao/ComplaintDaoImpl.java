package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ComplaintDaoImpl extends CrudDaoImpl<Complaint> implements ComplaintDao {

    private QueryLoader queryLoader;
    private UserDao userDao;

    @Autowired
    public ComplaintDaoImpl(QueryLoader queryLoader, UserDao userDao){
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
            preparedStatement.setString(1, entity.getStatus().name());
            preparedStatement.setDate(2, entity.getDate());
            preparedStatement.setString(3, entity.getText());
            preparedStatement.setString(4, entity.getType().name());
            preparedStatement.setObject(5, TypeMapper.getNullableId(entity.getUser()));
            preparedStatement.setString(6, entity.getSubject());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Complaint entity) {
        try {
            preparedStatement.setString(1, entity.getStatus().name());
            preparedStatement.setDate(2, entity.getDate());
            preparedStatement.setString(3, entity.getText());
            preparedStatement.setString(4, entity.getType().name());
            preparedStatement.setObject(5, TypeMapper.getNullableId(entity.getUser()));
            preparedStatement.setString(6, entity.getSubject());

            preparedStatement.setLong(7, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Complaint init(ResultSet rs) {
        Complaint complaint = new Complaint();
        try {
            complaint.setId(rs.getLong("id"));
            complaint.setStatus(ComplaintStatus.valueOf(rs.getString("status")));
            complaint.setDate(rs.getDate("date"));
            complaint.setText(rs.getString("text"));
            complaint.setType(ComplaintCategory.valueOf(rs.getString("type")));
            complaint.setUser(userDao.getById(rs.getLong("user_id")));
            complaint.setSubject(rs.getString("subject"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return complaint;
    }
}
