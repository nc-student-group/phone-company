package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.dao.interfaces.VerificationTokenDao;
import com.phonecompany.exception.EntityInitializationException;
import com.phonecompany.exception.PreparedStatementPopulationException;
import com.phonecompany.model.User;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.util.QueryLoader;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class VerificationTokenDaoImpl extends CrudDaoImpl<VerificationToken>
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
            preparedStatement.setString(1, vt.getToken());
            preparedStatement.setDate(2, TypeMapper.toSqlDate(vt.getExpireDate()));
            preparedStatement.setLong(3, TypeMapper.getNullableId(vt.getUser()));
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
            verificationToken.setExpireDate(TypeMapper.toLocalDate(rs.getDate("expire_date")));
        } catch (SQLException e) {
            throw new EntityInitializationException(e);
        }
        return verificationToken;
    }

    @Override
    public void populateUpdateStatement(PreparedStatement preparedStatement, VerificationToken vt) {

    }
}
