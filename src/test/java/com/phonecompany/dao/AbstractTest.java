package com.phonecompany.dao;

import com.phonecompany.Application;
import com.phonecompany.util.DbManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Yurii on 14.04.2017.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public abstract class AbstractTest {

    private DbManager dbManager = DbManager.getInstance();
    private final String BEGIN_TRANSACTION = "BEGIN;";
    private final String ROLLBACK_TRANSACTION = "ROLLBACK;";

    @Before
    public void setUp(){
        try(Connection conn = dbManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(BEGIN_TRANSACTION)) {
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @After
    public final void tearDown() {
        try(Connection conn = dbManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(ROLLBACK_TRANSACTION)) {
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }
}
