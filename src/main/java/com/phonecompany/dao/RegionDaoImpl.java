package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.RegionDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Region;
import com.phonecompany.util.interfaces.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RegionDaoImpl extends JdbcOperationsImpl<Region> implements RegionDao {

    private QueryLoader queryLoader;

    @Autowired
    public RegionDaoImpl(QueryLoader queryLoader){
        this.queryLoader = queryLoader;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.region."+type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, Region entity) {
        try {
            preparedStatement.setString(1, entity.getNameRegion());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, Region entity) {
        try {
            preparedStatement.setString(1, entity.getNameRegion());
            preparedStatement.setLong(2, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public Region init(ResultSet rs) {
        Region region = new Region();
        try {
            region.setId(rs.getLong("id"));
            region.setNameRegion(rs.getString("name_region"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return region;
    }
}
