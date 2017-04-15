package com.phonecompany;

import com.phonecompany.interfaces.AddressDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Address;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AddressDaoImpl extends CrudDaoImpl<Address> implements AddressDao {

    private QueryLoader queryLoader;

    @Autowired
    public AddressDaoImpl(QueryLoader queryLoader){
        this.queryLoader = queryLoader;
    }
    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.address."+type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Address address) {
        try {
            preparedStatement.setString(1, address.getCountry());
            preparedStatement.setString(2, address.getRegion());
            preparedStatement.setString(3, address.getSettlement());
            preparedStatement.setString(4, address.getStreet());
            preparedStatement.setString(5, address.getHouseNumber());
            preparedStatement.setString(6, address.getApartment());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Address init(ResultSet rs) {
        Address address = new Address();
        try {
            address.setId(rs.getLong("id"));
            address.setCountry(rs.getString("country"));
            address.setRegion(rs.getString("region"));
            address.setSettlement(rs.getString("settlement"));
            address.setStreet(rs.getString("street"));
            address.setHouseNumber(rs.getString("house_number"));
            address.setApartment(rs.getString("apartment"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return address;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Address address) {
        try {
            preparedStatement.setString(1, address.getCountry());
            preparedStatement.setString(2, address.getRegion());
            preparedStatement.setString(3, address.getSettlement());
            preparedStatement.setString(4, address.getStreet());
            preparedStatement.setString(5, address.getHouseNumber());
            preparedStatement.setString(6, address.getApartment());
            preparedStatement.setLong(7, address.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }
}
