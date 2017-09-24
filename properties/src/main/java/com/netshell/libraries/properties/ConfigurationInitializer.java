package com.netshell.libraries.properties;

/**
 * @author Abhishek
 * @since 5/22/2016.
 */
public interface ConfigurationInitializer {
    /**
     * Load the configuration.
     * The procedure it follows is as:
     * <ol>
     * <li>load default properties
     * <li>readConfiguration from {@code configInputStream}
     * <li>override properties if desired
     * <li>validate all properties
     * <li>hook for parsing properties
     * </ol>
     */
    default java.util.Properties loadConfig() {
        defaults();
        readConfig();
        getProperties().putAll(System.getProperties());
        overrideConfig();
        validateConfig();
        parseConfig();
        return getProperties();
    }

    /**
     * default properties to load
     */
    default void defaults() {

    }

    void readConfig();

    /**
     * @return all configured {@link java.util.Properties}
     */
    java.util.Properties getProperties();

    /**
     * a hook to override properties
     */
    default void overrideConfig() {

    }

    /**
     * Validation hook to check if properties are valid
     */
    default void validateConfig() {

    }

    /**
     * A hook after property initialization
     */
    default void parseConfig() {

    }
}
