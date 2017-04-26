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
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            statement.setLong(12, customer.getId());
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
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return customer;
    }

    @Override
    public Customer getByVerificationToken(String token) {
        String customerByVerificationTokenQuery = this.getByVerificationTokenQuery();
        LOG.debug("customerByVerificationTokenQuery : {}", customerByVerificationTokenQuery);
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(customerByVerificationTokenQuery)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return this.init(rs);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(token, e);
        }
        return null;
    }

    private String getByVerificationTokenQuery() {
        return this.getQuery("by.verification.token");
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.customer." + type);
    }

    public List<Customer> getAllCustomersPaging(int page, int size, long rId, String status) {
        List<Object> params = new ArrayList<>();
        String query = buildQuery(this.getQuery("getAllByRegionAndStatus"), params, rId, status);
        query += " LIMIT ? OFFSET ?";
        params.add(size);
        params.add(page * size);
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            LOG.info(query);
            ResultSet rs = ps.executeQuery();
            List<Customer> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    private String buildQuery(String query, List params, long rId, String status) {
        String where = " WHERE dbu.role_id=4";
        if (rId > 0) {
            query += " INNER JOIN address as a on dbu.address_id = a.id ";
            where += " and a.region_id = ?";
            params.add(rId);
        }
        if (!status.equals("ALL")) {
            where += " and dbu.status=?";
            params.add(status);
        }
        query += where;
        return query;
    }

    public int getCountCustomers(long rId, String status) {
        List<Object> params = new ArrayList<>();
        String query = buildQuery(this.getQuery("getCount"), params, rId, status);
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }
}
