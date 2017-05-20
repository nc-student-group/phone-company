package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.RegionDao;
import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.dao.interfaces.TariffRegionDao;
import com.phonecompany.exception.EntityDeletionException;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Region;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.util.interfaces.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TariffRegionDaoImpl extends JdbcOperationsImpl<TariffRegion> implements TariffRegionDao {

    private QueryLoader queryLoader;
    private RegionDao regionDao;
    private TariffDao tariffDao;

    @Autowired
    public TariffRegionDaoImpl(QueryLoader queryLoader, RegionDao regionDao, TariffDao tariffDao) {
        this.queryLoader = queryLoader;
        this.regionDao = regionDao;
        this.tariffDao = tariffDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.tariff_region." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, TariffRegion entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getRegion()));
            preparedStatement.setObject(2, TypeMapper.getNullableId(entity.getTariff()));
            preparedStatement.setDouble(3, entity.getPrice());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, TariffRegion entity) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(entity.getRegion()));
            preparedStatement.setObject(2, TypeMapper.getNullableId(entity.getTariff()));
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setLong(4, entity.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public TariffRegion init(ResultSet rs) {
        TariffRegion tariffRegion = new TariffRegion();
        try {
            tariffRegion.setId(rs.getLong("id"));
            tariffRegion.setRegion(regionDao.getById(rs.getLong("region_id")));
            tariffRegion.setTariff(tariffDao.getById(rs.getLong("tariff_id")));
            tariffRegion.setPrice(rs.getDouble("price"));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return tariffRegion;
    }

    @Override
    public List<TariffRegion> getAllByTariffId(Long tariffId) {
        List<TariffRegion> tariffRegions = new ArrayList<>();
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("getAllByTariffId"));
            ps.setLong(1, tariffId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TariffRegion tariffRegion = new TariffRegion();
                tariffRegion.setId(rs.getLong("tr_id"));
                tariffRegion.setRegion(new Region(rs.getLong("region_id"), rs.getString("name_region")));
                tariffRegion.setTariff(null);
                tariffRegion.setPrice(rs.getDouble("price"));
                tariffRegions.add(tariffRegion);
            }
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(tariffId, e);
        }finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
        return tariffRegions;
    }

    @Override
    public TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(this.getQuery("getAllByTariffIdAndRegionId"));
            ps.setLong(1, tariffId);
            ps.setLong(2, regionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return init(rs);
            }
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityNotFoundException(tariffId, e);
        }finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
        return null;
    }

    @Override
    public void deleteByTariffId(long tariffId) {
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(getQuery("deleteByTariffId"));
            ps.setLong(1, tariffId);
            ps.executeUpdate();
        } catch (SQLException e) {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
            throw new EntityDeletionException(tariffId, e);
        }finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(conn, this.getDataSource());
        }
    }
}
