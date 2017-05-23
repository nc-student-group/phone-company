package com.phonecompany.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.phonecompany.dao.interfaces.MarketingCampaignServicesDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.exception.dao_layer.EntityPersistenceException;
import com.phonecompany.exception.dao_layer.PreparedStatementPopulationException;
import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.MarketingCampaignServices;
import com.phonecompany.util.TypeMapper;
import com.phonecompany.util.interfaces.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MarketingCampaignServicesDaoImpl extends JdbcOperationsImpl<MarketingCampaignServices>
        implements MarketingCampaignServicesDao {

    @Autowired
    private ComboPooledDataSource dataSource;

    private QueryLoader queryLoader;
    private ServiceDao serviceDao;

    @Autowired
    public MarketingCampaignServicesDaoImpl(QueryLoader queryLoader, ServiceDao serviceDao) {
        this.queryLoader = queryLoader;
        this.serviceDao = serviceDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.marketing_campaign_services." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, MarketingCampaignServices entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getService()));
            preparedStatement.setDouble(2, entity.getPrice());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, MarketingCampaignServices entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getService()));
            preparedStatement.setDouble(2, entity.getPrice());
            preparedStatement.setLong(3, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public MarketingCampaignServices init(ResultSet rs) {
        MarketingCampaignServices marketingCampaignServices = new MarketingCampaignServices();
        try {
            marketingCampaignServices.setId(rs.getLong("id"));
            marketingCampaignServices.setService(serviceDao.getById(rs.getLong("service_id")));
            marketingCampaignServices.setPrice(rs.getDouble("price"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return marketingCampaignServices;
    }

    @Override
    public List<MarketingCampaignServices> getServicesByMarketingCampaignId(Long mcId) {
        return this.executeForList(this.getQuery("getByMarketingCampaignId"),
                new Object[]{mcId});
    }

    @Override
    public MarketingCampaignServices save(MarketingCampaignServices entity, MarketingCampaign marketingCampaign) {
        Connection conn = DataSourceUtils.getConnection(this.dataSource);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("save"));
            this.populateSaveStatement(ps, entity);
            ps.setObject(3, TypeMapper.getNullableId(marketingCampaign));
            ResultSet rs = ps.executeQuery();
            rs.next();
            long generatedId = rs.getLong(1);
            entity.setId(generatedId);
            return entity;
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
            throw new EntityPersistenceException(entity, e);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
    }
}