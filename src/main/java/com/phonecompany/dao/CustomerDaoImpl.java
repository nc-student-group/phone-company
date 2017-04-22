package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AddressDao;
import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.dao.interfaces.CustomerDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Customer;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CustomerDaoImpl extends AbstractUserDaoImpl<Customer>
        implements CustomerDao {

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
    public String getQuery(String type) {
        return queryLoader.getQuery("query.customer." + type);
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
            statement.setString(4, customer.getStatus().name());
            statement.setString(5, customer.getFirstName());
            statement.setString(6, customer.getSecondName());
            statement.setString(7, customer.getLastName());
            statement.setString(8, customer.getPhone());
            statement.setLong(9, TypeMapper.getNullableId(customer.getAddress()));
            statement.setLong(10, TypeMapper.getNullableId(customer.getCorporate()));
            statement.setBoolean(11, customer.getRepresentative());
            statement.setString(12, customer.getStatus().name());
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
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return customer;
    }

    @Override
    public Customer getUserByVerificationToken(String token) {
        String userByVerificationTokenQuery = this.getUserByVerificationTokenQuery();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(userByVerificationTokenQuery)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return this.init(rs);
        } catch (SQLException e) {
            throw new EntityNotFoundException(token, e);
        }
    }

    private String getUserByVerificationTokenQuery() {
        return this.getQuery("get.user.by.verification.token");
    }

}
