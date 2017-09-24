package com.netshell.libraries.dbmodules.dbcommon.util;

import com.netshell.libraries.test.core.DerbyUtils;
import com.netshell.libraries.test.core.UnitTestBase;
import org.junit.*;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Abhishek
 * on 10/15/2016.
 */
@PrepareForTest({DBUtil.class})
@SuppressStaticInitializationFor("com.netshell.libraries.dbmodules.dbcommon.util.JDBCProperties")
public class DBUtilTest extends UnitTestBase {

    private static final String DB_NAME = "DB_COMMON";
    private static final String TABLE_NAME = "DB_COMMON_TABLE";
    private static final String EXECUTE_UPDATE_QUERY_B = "SELECT * FROM " + TABLE_NAME + " WHERE NAME LIKE '%B%'";
    private static final String EXECUTE_UPDATE_QUERY_A = "SELECT * FROM " + TABLE_NAME + " WHERE NAME LIKE '%A%'";
    private static final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES('%s', %d)";
    private static final String INSERT_QUERY_PARAM = "INSERT INTO " + TABLE_NAME + " VALUES(?,?)";
    private static final String SCHEMA = "CREATE TABLE " + TABLE_NAME + "(NAME VARCHAR(20), AGE INT)";

    private DBUtil dbUtil;

    @BeforeClass
    public static void setupClass() throws Exception {
        DerbyUtils.setup(DB_NAME, SCHEMA);
        DerbyUtils.executeUpdate(DB_NAME, String.format(INSERT_QUERY, "A1", 1));
        DerbyUtils.executeUpdate(DB_NAME, String.format(INSERT_QUERY, "A2", 2));
        DerbyUtils.executeUpdate(DB_NAME, String.format(INSERT_QUERY, "A3", 3));
        DerbyUtils.executeUpdate(DB_NAME, String.format(INSERT_QUERY, "A4", 4));
        DerbyUtils.executeUpdate(DB_NAME, String.format(INSERT_QUERY, "A5", 5));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DerbyUtils.tearDown(DB_NAME, TABLE_NAME);
    }

    @Before
    public void setup() {
        mockProperties();
        mockDB();
    }

    private void mockProperties() {
        mockGetInstanceClass(JDBCProperties.class);
    }

    private void mockDB() {
        try {
            PowerMockito.mockStatic(DriverManager.class);
            Mockito.when(DriverManager.getConnection(
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString()
            )).thenReturn(DerbyUtils.getConnection(DB_NAME));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        DBUtil.closeConnection(false);
    }

    @Test
    public void executeQuery() throws Exception {
        DBUtil.executeQuery(EXECUTE_UPDATE_QUERY_A, Collections.emptyList(), resultSet -> {
            try {
                final List<String[]> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new String[]{resultSet.getString("NAME"), String.valueOf(resultSet.getInt("AGE"))});
                }

                Assert.assertEquals(5, list.size());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void executeUpdate() throws Exception {
        boolean success = true;
        try {
            Assert.assertEquals(1, DBUtil.executeUpdate(INSERT_QUERY_PARAM, Arrays.asList("B1", 1)));
            Assert.assertEquals(1, DBUtil.executeUpdate(INSERT_QUERY_PARAM, Arrays.asList("B2", 2)));
            Assert.assertEquals(1, DBUtil.executeUpdate(INSERT_QUERY_PARAM, Arrays.asList("B3", 3)));
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            DBUtil.closeConnection(success);
        }

        DerbyUtils.executeQuery(DB_NAME, EXECUTE_UPDATE_QUERY_B, resultSet -> {
            try {
                final List<String[]> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new String[]{resultSet.getString("NAME"), String.valueOf(resultSet.getInt("AGE"))});
                }

                Assert.assertEquals(3, list.size());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void executeProcedure() throws Exception {

    }

    @Test
    public void closeConnection() throws Exception {

    }

}