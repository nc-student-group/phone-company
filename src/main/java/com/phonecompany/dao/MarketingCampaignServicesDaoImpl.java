package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.MarketingCampaignDao;
import com.phonecompany.dao.interfaces.MarketingCampaignServicesDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.MarketingCampaignServices;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MarketingCampaignServicesDaoImpl extends CrudDaoImpl<MarketingCampaignServices>
        implements MarketingCampaignServicesDao {

    private QueryLoader queryLoader;
    private MarketingCampaignDao marketingCampaignDao;
    private ServiceDao serviceDao;

    @Autowired
    public MarketingCampaignServicesDaoImpl(QueryLoader queryLoader, MarketingCampaignDao marketingCampaignDao, ServiceDao serviceDao) {
        this.queryLoader = queryLoader;
        this.marketingCampaignDao = marketingCampaignDao;
        this.serviceDao = serviceDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.marketing_campaign_services." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, MarketingCampaignServices entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getMarketingCampaign()));
            preparedStatement.setObject(2, TypeMapper.getNullableId(entity.getService()));
            preparedStatement.setDouble(3, entity.getPrice());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, MarketingCampaignServices entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getMarketingCampaign()));
            preparedStatement.setObject(2, TypeMapper.getNullableId(entity.getService()));
            preparedStatement.setDouble(3, entity.getPrice());

            preparedStatement.setLong(4, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public MarketingCampaignServices init(ResultSet rs) {
        MarketingCampaignServices marketingCampaignServices = new MarketingCampaignServices();
        try {
            marketingCampaignServices.setId(rs.getLong("id"));
            marketingCampaignServices.setMarketingCampaign(marketingCampaignDao.getById(rs.getLong("marketing_campaign_id")));
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
}