package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AddressDao;
import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.exception.CrudException;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.enums.Status;
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

@SuppressWarnings("Duplicates")
@Repository
public class CustomerDaoImpl extends AbstractUserDaoImpl<Customer>
        implements CustomerDao {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDaoImpl.class);

    private QueryLoader queryLoader;
    private AddressDao addressDao;
    private CorporateDao corporateDao;

    @Autowired
    public CustomerDaoImpl(QueryLoader queryLoader,
                           AddressDao addressDao,
                           CorporateDao corporateDao) {
        this.queryLoader = queryLoader;
        this.addressDao = addressDao;
        this.corporateDao = corporateDao;
    }

    @Override
    public void populateSaveStatement(PreparedStatement statement, Customer customer) {
        try {
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setLong(3, customer.getRole().getDatabaseId());
            statement.setString(4, customer.getFirstName());
            statement.setString(5, customer.getSecondName());
            statement.setString(6, customer.getLastName());
            statement.setString(7, customer.getPhone());
            statement.setObject(8, TypeMapper.getNullableId(customer.getAddress()));
            statement.setObject(9, TypeMapper.getNullableId(customer.getCorporate()));
            statement.setObject(10, customer.getRepresentative());
            statement.setString(11, customer.getStatus().name());
            statement.setBoolean(12, customer.getIsMailingEnabled());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement statement, Customer customer) {
        try {
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setLong(3, customer.getRole().getDatabaseId());
            statement.setString(4, customer.getFirstName());
            statement.setString(5, customer.getSecondName());
            statement.setString(6, customer.getLastName());
            statement.setString(7, customer.getPhone());
            statement.setObject(8, TypeMapper.getNullableId(customer.getAddress()));
            statement.setObject(9, TypeMapper.getNullableId(customer.getCorporate()));
            statement.setObject(10, customer.getRepresentative());
            statement.setString(11, customer.getStatus().name());
            statement.setBoolean(12, customer.getIsMailingEnabled());
            statement.setLong(13, customer.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Customer init(ResultSet rs) {
        Customer customer = new Customer();
        try {
            customer.setId(rs.getLong("id"));
            customer.setEmail(rs.getString("email"));
            customer.setPassword(rs.getString("password"));
            customer.setRole(TypeMapper.getUserRoleByDatabaseId(rs.getLong("role_id")));
            customer.setStatus(Status.valueOf(rs.getString("status")));
            customer.setFirstName(rs.getString("firstname"));
            customer.setSecondName(rs.getString("secondname"));
            customer.setLastName(rs.getString("lastname"));
            customer.setPhone(rs.getString("phone"));
            customer.setAddress(addressDao.getById(rs.getLong("address_id")));
            customer.setCorporate(corporateDao.getById(rs.getLong("corporate_id")));
            customer.setRepresentative(rs.getBoolean("is_representative"));
            customer.setStatus(Status.valueOf(rs.getString("status")));
            customer.setIsMailingEnabled(rs.getBoolean("is_mailing_enabled"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return customer;
    }

    @Override
    public Customer getByVerificationToken(String token) {
        String customerByVerificationTokenQuery = this.getByVerificationTokenQuery();
        LOG.debug("customerByVerificationTokenQuery : {}", customerByVerificationTokenQuery);
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(customerByVerificationTokenQuery);
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return this.init(rs);
            }
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(token, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
        return null;
    }

    @Override
    public List<Customer> getByCorporateId(long corporateId) {
        if (corporateId > 0) {
            return getByCorporate(corporateId);
        } else {
            return getCustomersWithoutCorporate(corporateId);
        }
    }

    private List<Customer> getByCorporate(long corporateId) {
        String customersByCorporate = this.getByCorporateIdQuery();
        LOG.debug("customerByCompany : {}", customersByCorporate);
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(customersByCorporate);
            ps.setLong(1, corporateId);
            ResultSet rs = ps.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(this.init(rs));
            }
            return customers;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(corporateId, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    private List<Customer> getCustomersWithoutCorporate(long corporateId) {
        String customersByCorporate = this.getWithoutCorporateQuery();
        LOG.debug("customerWithoutCorporate : {}", customersByCorporate);
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(customersByCorporate);
            ResultSet rs = ps.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(this.init(rs));
            }
            return customers;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(corporateId, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }

    @Override
    public int getCountByPhone(String phone) {
        return this.getCountByKey(phone, this.getCountByPhoneQuery());
    }




    private String getCountByPhoneQuery() {
        return this.getQuery("count.by.phone");
    }

    private String getByVerificationTokenQuery() {
        return this.getQuery("by.verification.token");
    }

    private String getByCorporateIdQuery() {
        return this.getQuery("by.corporate");
    }

    private String getWithoutCorporateQuery() {
        return this.getQuery("without.corporate");
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.customer." + type);
    }

    @Override
    public String prepareWhereClause(Object... args) {

        String where = "";

        long regionId = (long) args[0];
        String status = (String) args[1];

        if (regionId > 0) {
            where += " AND address.region_id = ?";
            this.preparedStatementParams.add(regionId);
        }
        if (!status.equals("ALL")) {
            where += " AND dbuser.status = ?";
            this.preparedStatementParams.add(status);
        }

        return where;
    }

    @Override
    public List<Customer> getAllCustomersSearch(Query query) {
        Connection conn = DataSourceUtils.getConnection(this.getDataSource());
        PreparedStatement ps = null;

        try {
            LOG.info("Execute query: " + query.getQuery());
            ps = conn.prepareStatement(query.getQuery());

            for(int i = 0; i<query.getPreparedStatementParams().size();i++){
                ps.setObject(i+1,query.getPreparedStatementParams().get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<Customer> result = new ArrayList<>();
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
