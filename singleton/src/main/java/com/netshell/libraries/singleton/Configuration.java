package com.netshell.libraries.singleton;

import com.netshell.libraries.properties.AbstractConfiguration;
import com.netshell.libraries.properties.Properties;

/**
 * Singleton configurations.
 *
 * @author Abhishek
 * @since 3/23/2016.
 */
final class Configuration extends AbstractConfiguration {

    /**
     * Name of properties file to read the configuration from.
     */
    private static final String SINGLETON_MANAGER_PROPERTIES = "singleton_manager.properties";

    /**
     * Initial Capacity Parameter Name
     */
    private static final String INITIAL_CAPACITY = "SingletonManager.InitialCapacity";

    /**
     * Singleton instance
     */
    private static final Configuration instance;

    static {
        instance = Properties.createConfiguration(Configuration.class, SINGLETON_MANAGER_PROPERTIES);
    }

    /**
     * Initial Capacity of the Manager
     */
    private int initialCapacity = 10;

    /**
     * @return singleton instance of the configuration.
     */
    public static Configuration getInstance() {
        return instance;
    }

    /**
     * @return initial capacity of the manager
     */
    public int getInitialCapacity() {
        return initialCapacity;
    }

    /**
     * read initialCapacity
     */
    @Override
    protected void parseConfiguration() {
        initialCapacity = Integer.parseInt(getProperties().getProperty(INITIAL_CAPACITY));
    }
}
