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
        return executeForObject(this.getQuery("by.verification.token"), new Object[]{token});
    }

    @Override
    public List<Customer> getByCorporateId(long corporateId) {
        if (corporateId > 0) {
            return getByCorporate(corporateId);
        } else {
            return getCustomersWithoutCorporate();
        }
    }

    private List<Customer> getByCorporate(long corporateId) {
        return this.executeForList(this.getQuery("by.corporate"), new Object[]{corporateId});
    }

    private List<Customer> getCustomersWithoutCorporate() {
        return this.executeForList(this.getQuery("without.corporate"), new Object[]{});
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
    public List<Customer> getAllCustomersSearch(Query query) {
        return executeForList(query.getQuery(), query.getPreparedStatementParams().toArray());
    }
}
