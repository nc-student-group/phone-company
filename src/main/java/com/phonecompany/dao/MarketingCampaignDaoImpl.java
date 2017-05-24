package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.MarketingCampaignDao;
import com.phonecompany.dao.interfaces.MarketingCampaignServicesDao;
import com.phonecompany.dao.interfaces.TariffRegionDao;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.exception.dao_layer.PreparedStatementPopulationException;
import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.MarketingCampaignServices;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.TypeMapper;
import com.phonecompany.util.interfaces.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MarketingCampaignDaoImpl extends JdbcOperationsImpl<MarketingCampaign>
        implements MarketingCampaignDao {

    private QueryLoader queryLoader;
    private TariffRegionDao tariffRegionDao;
    private MarketingCampaignServicesDao marketingCampaignServicesDao;

    @Autowired
    public MarketingCampaignDaoImpl(QueryLoader queryLoader,
                                    TariffRegionDao tariffRegionDao,
                                    MarketingCampaignServicesDao marketingCampaignServicesDao) {
        this.queryLoader = queryLoader;
        this.tariffRegionDao = tariffRegionDao;
        this.marketingCampaignServicesDao = marketingCampaignServicesDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.marketing_campaign." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, MarketingCampaign entity) {
        try {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getMarketingCampaignStatus().name());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setObject(4, TypeMapper.getNullableId(entity.getTariffRegion()));
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, MarketingCampaign entity) {
        try {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getMarketingCampaignStatus().name());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setObject(4, TypeMapper.getNullableId(entity.getTariffRegion()));
            preparedStatement.setLong(5, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public MarketingCampaign init(ResultSet rs) {
        MarketingCampaign marketingCampaign = new MarketingCampaign();
        try {
            marketingCampaign.setId(rs.getLong("id"));
            marketingCampaign.setName(rs.getString("name"));
            marketingCampaign.setMarketingCampaignStatus(ProductStatus.valueOf(rs.getString("marketing_campaign_status")));
            marketingCampaign.setDescription(rs.getString("description"));
            marketingCampaign.setTariffRegion(tariffRegionDao.getById(rs.getLong("tariff_region_id")));
            marketingCampaign.setServices(marketingCampaignServicesDao.getServicesByMarketingCampaignId(rs.getLong("id")));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return marketingCampaign;
    }

    @Override
    public List<MarketingCampaign> getAllByTariffRegion(Long tariffRegionId) {
        return this.executeForList(this.getQuery("getAllByTariffRegion"),
                new Object[]{tariffRegionId});
    }

    @Override
    public void updateMarketingCampaignStatus(Long campaignId, ProductStatus productStatus) {
        this.executeUpdate(this.getQuery("updateStatus"), new Object[]{productStatus.name(), campaignId});
    }
}
