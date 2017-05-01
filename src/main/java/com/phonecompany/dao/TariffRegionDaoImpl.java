package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.RegionDao;
import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.dao.interfaces.TariffRegionDao;
import com.phonecompany.exception.EntityDeletionException;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.EntityNotFoundException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.Region;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TariffRegionDaoImpl extends CrudDaoImpl<TariffRegion> implements TariffRegionDao {

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
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getAllByTariffId"))) {
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
            throw new EntityNotFoundException(tariffId, e);
        }
        return tariffRegions;
    }

    @Override
    public TariffRegion getByTariffIdAndRegionId(Long tariffId, long regionId) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getAllByTariffIdAndRegionId"))) {
            ps.setLong(1, tariffId);
            ps.setLong(2, regionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return init(rs);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(tariffId, e);
        }
        return null;
    }

    @Override
    public void deleteByTariffId(long tariffId) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(getQuery("deleteByTariffId"))) {
            preparedStatement.setLong(1, tariffId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EntityDeletionException(tariffId, e);
        }
    }
}
