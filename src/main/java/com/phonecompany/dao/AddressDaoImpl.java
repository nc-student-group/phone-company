package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.AddressDao;
import com.phonecompany.dao.interfaces.RegionDao;
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
public class AddressDaoImpl extends CrudDaoImpl<Address>
        implements AddressDao {

    private QueryLoader queryLoader;
    private RegionDao regionDao;

    @Autowired
    public AddressDaoImpl(QueryLoader queryLoader,
                          RegionDao regionDao){
        this.queryLoader = queryLoader;
        this.regionDao = regionDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.address."+type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Address address) {
        try {
            preparedStatement.setLong(1, address.getRegion().getId());
            preparedStatement.setString(2, address.getStreet());
            preparedStatement.setLong(3, address.getHouseNumber());
            preparedStatement.setString(4, address.getApartmentNumber());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Address init(ResultSet rs) {
        Address address = new Address();
        try {
            address.setId(rs.getLong("id"));
            address.setRegion(regionDao.getById(rs.getLong("region_id")));
//            address.setSettlement(rs.getString("settlement"));
            address.setStreet(rs.getString("street"));
            address.setHouseNumber(rs.getLong("house_number"));
            address.setApartmentNumber(rs.getString("apartment_number"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return address;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Address address) {
        try {
//            preparedStatement.setLong(1, address.getRegionId());
            preparedStatement.setString(2, address.getStreet());
            preparedStatement.setString(3, address.getStreet());
            preparedStatement.setLong(4, address.getHouseNumber());
            preparedStatement.setString(5, address.getApartmentNumber());
            preparedStatement.setLong(6, address.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }
}
