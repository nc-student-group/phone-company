package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.dao.interfaces.VerificationTokenDao;
import com.phonecompany.exception.dao_layer.EntityInitializationException;
import com.phonecompany.exception.dao_layer.PreparedStatementPopulationException;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.util.interfaces.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class VerificationTokenDaoImpl extends JdbcOperationsImpl<VerificationToken>
        implements VerificationTokenDao {

    private QueryLoader queryLoader;
    private UserDao userDao;

    @Autowired
    public VerificationTokenDaoImpl(QueryLoader queryLoader,
                                    UserDao userDao) {
        this.queryLoader = queryLoader;
        this.userDao = userDao;
    }

    @Override
    public String getQuery(String type) {
        return queryLoader.getQuery("query.verification_token." + type);
    }

    @Override
    public void populateSaveStatement(PreparedStatement preparedStatement, VerificationToken vt) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(vt.getUser()));
            preparedStatement.setString(2, vt.getToken());
            preparedStatement.setDate(3, TypeMapper.toSqlDate(vt.getExpireDate()));
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }

    @Override
    public VerificationToken init(ResultSet rs) {
        VerificationToken verificationToken = new VerificationToken();
        try {
            verificationToken.setId(rs.getLong("id"));
            verificationToken.setUser(userDao.getById(rs.getLong("user_id")));
            verificationToken.setToken(rs.getString("token"));
            verificationToken.setExpireDate(TypeMapper.toLocalDate(rs.getDate("expiry_date")));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return verificationToken;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, VerificationToken vt) {
        try {
            preparedStatement.setObject(1, TypeMapper.getNullableId(vt.getUser()));
            preparedStatement.setString(2, vt.getToken());
            preparedStatement.setDate(3, TypeMapper.toSqlDate(vt.getExpireDate()));
            preparedStatement.setLong(4, vt.getId());
        } catch (SQLException e) {
            throw new PreparedStatementPopulationException(e);
        }
    }
}
