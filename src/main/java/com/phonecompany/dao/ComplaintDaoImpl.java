package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.Customer;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.util.Query;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ComplaintDaoImpl extends AbstractPageableDaoImpl<Complaint> implements ComplaintDao {

    private QueryLoader queryLoader;
    private UserDao userDao;
    private static final Logger LOG = LoggerFactory.getLogger(ComplaintDaoImpl.class);
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
            preparedStatement.setDate(2, TypeMapper.toSqlDate(entity.getDate()));
            preparedStatement.setString(3, entity.getText());
            preparedStatement.setString(4, entity.getType().name());
            preparedStatement.setObject(5, TypeMapper.getNullableId(entity.getUser()));
            preparedStatement.setString(6, entity.getSubject());
            preparedStatement.setObject(7,TypeMapper.getNullableId(entity.getResponsiblePmg()));
            preparedStatement.setString(8, entity.getComment());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Complaint entity) {
        try {
            preparedStatement.setString(1, entity.getStatus().name());
            preparedStatement.setDate(2, TypeMapper.toSqlDate(entity.getDate()));
            preparedStatement.setString(3, entity.getText());
            preparedStatement.setString(4, entity.getType().name());
            preparedStatement.setObject(5, TypeMapper.getNullableId(entity.getUser()));
            preparedStatement.setString(6, entity.getSubject());
            preparedStatement.setObject(7,TypeMapper.getNullableId(entity.getResponsiblePmg()));
            preparedStatement.setString(8, entity.getComment());

            preparedStatement.setLong(9, entity.getId());
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
            complaint.setDate(TypeMapper.toLocalDate(rs.getDate("date")));
            complaint.setText(rs.getString("text"));
            complaint.setType(ComplaintCategory.valueOf(rs.getString("type")));
            complaint.setUser(userDao.getById(rs.getLong("user_id")));
            complaint.setSubject(rs.getString("subject"));
            complaint.setResponsiblePmg(userDao.getById(rs.getLong("responsible_pmg")));
            complaint.setComment(rs.getString("comment"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return complaint;
    }

    @Override
    public String prepareWhereClause(Object... args) {

        String where = "";
        String category = (String) args[0];
        String status = (String) args[1];
        Long userId = (long) args[2];
        Long responsibleId = (long) args[3];

        if ((!category.equals("-"))||(!status.equals("-"))||(userId > 0)) where += " WHERE ";

        boolean moreThenOne = false;
        if (!category.equals("-")) {
            where += " type = ? ";
            this.preparedStatementParams.add(category);
            moreThenOne = true;
        }
        if (!status.equals("-")) {
            where += moreThenOne ? " and status = ? " : " status = ? ";
            this.preparedStatementParams.add(status);
            moreThenOne = true;
        }
        if (userId > 0) {
            where += moreThenOne ? " and user_id = ? " : " user_id = ? ";
            this.preparedStatementParams.add(userId);
            moreThenOne = true;
        }
        if (responsibleId > 0) {
            where += moreThenOne ? " and responsible_pmg = ? " : " responsible_pmg = ? ";
            this.preparedStatementParams.add(responsibleId);
            //moreThenOne = true;
        }
        return where;
    }

    @Override
    public List<Complaint> getAllComplaintsSearch(Query query) {
        Connection conn = DataSourceUtils.getConnection(this.getDataSource());
        PreparedStatement ps = null;
        LOG.info("Execute query: " + query.getQuery());
        try {
            ps = conn.prepareStatement(query.getQuery());

            for(int i = 0; i<query.getPreparedStatementParams().size();i++){
                ps.setObject(i+1,query.getPreparedStatementParams().get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<Complaint> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }
}
