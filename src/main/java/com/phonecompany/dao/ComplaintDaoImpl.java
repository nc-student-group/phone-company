package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.model.enums.WeekOfMonth;
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
import java.util.EnumMap;
import java.util.List;

@Repository
public class ComplaintDaoImpl extends AbstractPageableDaoImpl<Complaint> implements ComplaintDao {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintDaoImpl.class);

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
    public EnumMap<WeekOfMonth, Integer> getNumberOfComplaintsForTheLastMonthByCategory(ComplaintCategory type) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(
                    this.getQuery("for.the.last.month.by.type"));
            ps.setString(1, type.name());
            ResultSet rs = ps.executeQuery();
            return this.associateWeeksWithComplaintNumbers(rs);
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new CrudException("Could not extract complaints numbers", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    private EnumMap<WeekOfMonth, Integer> associateWeeksWithComplaintNumbers(ResultSet rs)
            throws SQLException {
        EnumMap<WeekOfMonth, Integer> result = new EnumMap<>(WeekOfMonth.class);
        WeekOfMonth weekOfMonth = WeekOfMonth.FIRST_WEEK;
        while (rs.next()) {
            result.put(weekOfMonth, rs.getInt(1));
            LOG.debug("weekOfMonth.next(): {}", weekOfMonth.next());
            weekOfMonth = weekOfMonth.next();
        }
        return result;
    }

    @Override
    public List<Complaint> getComplaintsByRegionId(Long regionId){
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("by.region.id"));
            ps.setLong(1, regionId);
            ResultSet rs = ps.executeQuery();
            List<Complaint> complaints = new ArrayList<>();
            while (rs.next()) {
                complaints.add(this.init(rs));
            }
            return complaints;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(regionId, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

}
