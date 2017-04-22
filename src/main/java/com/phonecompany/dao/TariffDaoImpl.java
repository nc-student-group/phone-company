package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TariffDaoImpl extends CrudDaoImpl<Tariff> implements TariffDao {

    private QueryLoader queryLoader;

    @Autowired
    public TariffDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.tariff."+type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Tariff entity) {
        try {
            preparedStatement.setString(1, entity.getTariffName());
            preparedStatement.setString(2, entity.getProductStatus().name());
            preparedStatement.setString(3, entity.getInternet());
            preparedStatement.setString(4, entity.getCallsInNetwork());
            preparedStatement.setString(5, entity.getCallsOnOtherNumbers());
            preparedStatement.setString(6, entity.getSms());
            preparedStatement.setString(7, entity.getMms());
            preparedStatement.setString(8, entity.getRoaming());
            preparedStatement.setBoolean(9, entity.getIsCorporate());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Tariff entity) {
        try {
            preparedStatement.setString(1, entity.getTariffName());
            preparedStatement.setString(2, entity.getProductStatus().name());
            preparedStatement.setString(3, entity.getInternet());
            preparedStatement.setString(4, entity.getCallsInNetwork());
            preparedStatement.setString(5, entity.getCallsOnOtherNumbers());
            preparedStatement.setString(6, entity.getSms());
            preparedStatement.setString(7, entity.getMms());
            preparedStatement.setString(8, entity.getRoaming());
            preparedStatement.setBoolean(9, entity.getIsCorporate());

            preparedStatement.setLong(9, entity.getId());
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
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return tariff;
    }
}
