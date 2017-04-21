package com.phonecompany.dao;

import com.phonecompany.util.DbManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

    protected DbManager dbManager = DbManager.getInstance();
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
