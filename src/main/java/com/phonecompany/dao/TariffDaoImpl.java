package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.exception.dao_layer.PreparedStatementPopulationException;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.Query;
import com.phonecompany.util.interfaces.QueryLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

import static com.phonecompany.util.TypeMapper.toLocalDate;
import static com.phonecompany.util.TypeMapper.toSqlDate;

@Repository
public class TariffDaoImpl extends JdbcOperationsImpl<Tariff> implements TariffDao {

    private QueryLoader queryLoader;

    @Autowired
    public TariffDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.tariff." + type);
    }


    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Tariff tariff) {
        try {
            preparedStatement.setString(1, tariff.getTariffName());
            preparedStatement.setString(2, tariff.getProductStatus().name());
            preparedStatement.setString(3, tariff.getInternet());
            preparedStatement.setString(4, tariff.getCallsInNetwork());
            preparedStatement.setString(5, tariff.getCallsOnOtherNumbers());
            preparedStatement.setString(6, tariff.getSms());
            preparedStatement.setString(7, tariff.getMms());
            preparedStatement.setString(8, tariff.getRoaming());
            preparedStatement.setBoolean(9, tariff.getIsCorporate());
            preparedStatement.setDate(10, toSqlDate(tariff.getCreationDate()));
            preparedStatement.setDouble(11, tariff.getDiscount());
            preparedStatement.setString(12, tariff.getPictureUrl());
            preparedStatement.setDouble(13, tariff.getPrice());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Tariff tariff) {
        try {
            preparedStatement.setString(1, tariff.getTariffName());
            preparedStatement.setString(2, tariff.getProductStatus().name());
            preparedStatement.setString(3, tariff.getInternet());
            preparedStatement.setString(4, tariff.getCallsInNetwork());
            preparedStatement.setString(5, tariff.getCallsOnOtherNumbers());
            preparedStatement.setString(6, tariff.getSms());
            preparedStatement.setString(7, tariff.getMms());
            preparedStatement.setString(8, tariff.getRoaming());
            preparedStatement.setBoolean(9, tariff.getIsCorporate());
            preparedStatement.setDate(10, toSqlDate(tariff.getCreationDate()));
            preparedStatement.setDouble(11, tariff.getDiscount());
            preparedStatement.setString(12, tariff.getPictureUrl());
            preparedStatement.setDouble(13, tariff.getPrice());
            preparedStatement.setLong(14, tariff.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Tariff init(ResultSet rs) {
        Tariff tariff = new Tariff();
        try {
            tariff.setId(rs.getLong("id"));
            tariff.setTariffName(rs.getString("tariff_name"));
            tariff.setProductStatus(ProductStatus.valueOf(rs.getString("product_status")));
            tariff.setInternet(rs.getString("internet"));
            tariff.setCallsInNetwork(rs.getString("calls_in_network"));
            tariff.setCallsOnOtherNumbers(rs.getString("calls_on_other_numbers"));
            tariff.setSms(rs.getString("sms"));
            tariff.setMms(rs.getString("mms"));
            tariff.setRoaming(rs.getString("roaming"));
            tariff.setIsCorporate(rs.getBoolean("is_corporate"));
            tariff.setCreationDate(toLocalDate(rs.getDate("creation_date")));
            tariff.setDiscount(rs.getDouble("discount"));
            tariff.setPictureUrl(rs.getString("picture_url"));
            tariff.setPrice(rs.getDouble("price"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return tariff;
    }


    @Override
    public void updateTariffStatus(long tariffId, ProductStatus productStatus) {
        this.executeUpdate(this.getQuery("updateStatus"), new Object[]{productStatus.name(), tariffId});
    }

    @Override
    public Tariff findByTariffName(String tariffName) {
        return this.executeForObject(this.getQuery("findByTariffName"), new Object[]{tariffName});
    }


    @Override
    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size) {
        return this.executeForList(this.getQuery("getAllWithRegionPrice"), new Object[]{
                regionId, size, size * page});
    }

    @Override
    public List<Tariff> getTariffsAvailableForCustomer(long regionId) {
        return this.executeForList(this.getQuery("getAllActivatedWithRegionPrice"), new Object[]{regionId});
    }

    @Override
    public Integer getCountTariffsAvailableForCustomer(long regionId) {
        return this.executeForInt(this.getQuery("getCountWithRegionPrice"), new Object[]{regionId});
    }

    @Override
    public List<Tariff> getTariffsAvailableForCorporate(int page, int size) {
        return this.executeForList(this.getQuery("getTariffsAvailableForCorporate"), new Object[]{size, size * page});
    }

    @Override
    public List<Tariff> getTariffsAvailableForCorporate() {
        return this.executeForList(this.getQuery("getAllTariffsAvailableForCorporate"), new Object[]{});
    }

    @Override
    public Integer getCountTariffsAvailableForCorporate() {
        return this.executeForInt(this.getQuery("getCountAvailableForCorporate"), new Object[]{});
    }

    @Override
    public Tariff getByIdForSingleCustomer(long id, long regionId) {
        return this.executeForObject(this.getQuery("getByIdForSingleCustomer"), new Object[]{id, regionId});
    }

    @Override
    public List<Tariff> getAllTariffsSearch(Query query) {
        return this.executeForList(query.getQuery(), query.getPreparedStatementParams().toArray());
    }

}
