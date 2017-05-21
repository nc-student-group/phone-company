package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.ComplaintDao;
import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.dao_layer.CrudException;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.exception.dao_layer.PreparedStatementPopulationException;
import com.phonecompany.model.Complaint;
import com.phonecompany.model.ComplaintStatistics;
import com.phonecompany.service.xssfHelper.Statistics;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;
import com.phonecompany.util.Query;
import com.phonecompany.model.enums.WeekOfMonth;
import com.phonecompany.util.interfaces.QueryLoader;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap;

import static com.phonecompany.util.TypeMapper.getEnumValueByDatabaseId;
import static com.phonecompany.util.TypeMapper.toSqlDate;

@Repository
public class ComplaintDaoImpl extends JdbcOperationsImpl<Complaint> implements ComplaintDao {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintDaoImpl.class);

    private QueryLoader queryLoader;
    private UserDao userDao;

    @Autowired
    public ComplaintDaoImpl(QueryLoader queryLoader, UserDao userDao) {
        this.queryLoader = queryLoader;
        this.userDao = userDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.complaint." + type);
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
            preparedStatement.setObject(7, TypeMapper.getNullableId(entity.getResponsiblePmg()));
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
            preparedStatement.setObject(7, TypeMapper.getNullableId(entity.getResponsiblePmg()));
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
    public List<Complaint> getAllComplaintsSearch(Query query) {
        return this.executeForList(query.getQuery(), query.getPreparedStatementParams().toArray());
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
        while (rs.next()) {
            long weekNumber = rs.getLong("week_number");
            int numberOfOrders = rs.getInt("number_of_complaints");
            WeekOfMonth weekOfMonth = getEnumValueByDatabaseId(WeekOfMonth.class, weekNumber);
            result.put(weekOfMonth, numberOfOrders);
        }
        return result;
    }

    @Override
    public List<Complaint> getComplaintsByRegionId(Long regionId) {
        return this.executeForList(this.getQuery("by.region.id"), new Object[]{regionId});
    }

    @Override
    public List<Statistics> getComplaintStatisticsByRegionAndTimePeriod(long regionId,
                                                                        LocalDate startDate,
                                                                        LocalDate endDate) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("by.region.id.and.time.period"));
            ps.setLong(1, regionId);
            ps.setDate(2, toSqlDate(startDate));
            ps.setDate(3, toSqlDate(endDate));
            ResultSet rs = ps.executeQuery();
            List<Statistics> statisticsList = new ArrayList<>();
            while (rs.next()) {
                statisticsList.add(this.createComplaintStatisticsObject(rs));
            }
            return statisticsList;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new CrudException("Could not extract service orders", e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    private ComplaintStatistics createComplaintStatisticsObject(ResultSet rs) throws SQLException {
        return new ComplaintStatistics(rs.getLong("complaint_count"),
                rs.getString("type"),
                ComplaintStatus.valueOf(rs.getString("status")),
                TypeMapper.toLocalDate(rs.getDate("date")));
    }
}
