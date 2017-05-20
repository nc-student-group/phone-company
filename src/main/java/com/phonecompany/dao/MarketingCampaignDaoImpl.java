package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.MarketingCampaignDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.MarketingCampaign;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.interfaces.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class MarketingCampaignDaoImpl extends JdbcOperationsImpl<MarketingCampaign>
        implements MarketingCampaignDao {

    private QueryLoader queryLoader;

    @Autowired
    public MarketingCampaignDaoImpl(QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
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

            preparedStatement.setLong(4, entity.getId());
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
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return marketingCampaign;
    }
}
