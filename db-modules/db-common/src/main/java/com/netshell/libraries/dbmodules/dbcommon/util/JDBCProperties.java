package com.netshell.libraries.dbmodules.dbcommon.util;

import com.netshell.libraries.properties.AbstractConfiguration;
import com.netshell.libraries.properties.InputStreamConfigurationInitializerImpl;
import com.netshell.libraries.properties.Properties;

/**
 * Created by Abhishek
 * on 10/15/2016.
 */
public final class JDBCProperties extends AbstractConfiguration {

    /**
     * Name of properties file to read the configuration from.
     */
    private static final String JDBC_PROPERTIES = "jdbc.properties";

    private static final String JDBC_DRIVER = "com.netshell.jdbc.driver";
    private static final String JDBC_CONNECTION_STRING = "com.netshell.jdbc.connection";
    private static final String JDBC_USER = "com.netshell.jdbc.user";
    private static final String JDBC_PASSWORD = "com.netshell.jdbc.password";

    /**
     * Singleton instance
     */
    private static final JDBCProperties instance;

    static {
        instance = Properties.createConfiguration(JDBCProperties.class, new Initializer());
    }

    /**
     * @return singleton instance of the configuration.
     */
    public static JDBCProperties getInstance() {
        return instance;
    }

    public String getJdbcDriver() {
        return getProperty(JDBC_DRIVER);
    }

    public String getJdbcConnectionString() {
        return getProperty(JDBC_CONNECTION_STRING);
    }

    public String getJdbcUser() {
        return getProperty(JDBC_USER);
    }

    public String getJdbcPassword() {
        return getProperty(JDBC_PASSWORD);
    }

    private static final class Initializer extends InputStreamConfigurationInitializerImpl {
        Initializer() {
            super(JDBCProperties.class.getClassLoader().getResourceAsStream(JDBC_PROPERTIES));
        }

        @Override
        public void validateConfig() {
            try {
                Class.forName(this.getProperties().getProperty(JDBC_DRIVER));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
