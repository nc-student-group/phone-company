package com.phonecompany.dao;

import com.phonecompany.dao.CrudDaoImpl;
import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.Complaint;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        return null;
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Complaint entity) {

    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Complaint entity) {

    }

    @Override
    public Complaint init(ResultSet resultSet) {
        return null;
    }
}
