package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.exception.*;
import com.phonecompany.model.DomainEntity;
import com.phonecompany.util.DbManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A base class that provides an abstract implementation for
 * all the methods applicable to any entity in the system
 * (e.g. save or update)
 *
 * @param <T> entity type
 */
public abstract class CrudDaoImpl<T extends DomainEntity>
        implements CrudDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CrudDaoImpl.class);

    DbManager dbManager = DbManager.getInstance();

    private boolean autoCommit = true;

    private final String BEGIN_TRANSACTION = "BEGIN;";
    private final String COMMIT_TRANSACTION = "COMMIT;";
    private final String ROLLBACK_TRANSACTION = "ROLLBACK;";

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T save(T entity) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("save"))) {
            conn.setAutoCommit(this.autoCommit);
            LOG.debug("Saving entity: {}", entity);
            this.populateSaveStatement(ps, entity);
            ResultSet rs = ps.executeQuery();
            rs.next();
            long generatedId = rs.getLong(1);
            entity.setId(generatedId);
            return entity;
        } catch (SQLException e) {
            throw new EntityPersistenceException(entity, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(T entity) {
        LOG.debug("Getting query: {}", this.getQuery("update"));
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("update"))) {
            conn.setAutoCommit(this.autoCommit);
            this.populateUpdateStatement(ps, entity);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EntityModificationException(entity, e);
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getById(Long id) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getById"))) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return init(rs);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException(id, e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.getQuery("getAll"))) {
            ResultSet rs = ps.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(init(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new CrudException("Failed to load all the entities. " +
                    "Check your database connection or whether sql query is right", e);
        }
    }

    @Override
    public void beginTransaction(){
        try(Connection conn = dbManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(BEGIN_TRANSACTION)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionBeginException(e);
        }
    }

    @Override
    public void commit(){
        try(Connection conn = dbManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(COMMIT_TRANSACTION)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionCommitException(e);
        }
    }

    @Override
    public void rollback(){
        try(Connection conn = dbManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(ROLLBACK_TRANSACTION)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionCommitException(e);
        }
    }

    public abstract String getQuery(String type);

    public abstract void populateSaveStatement(PreparedStatement preparedStatement, T entity);

    public abstract void populateUpdateStatement(PreparedStatement preparedStatement, T entity);

    public abstract T init(ResultSet resultSet);
}
