package com.netshell.libraries.properties;

import java.util.Properties;

/**
 * Defines set of properties configured for the current application
 *
 * @author Abhishek
 * @since 5/12/2016.
 */
public interface Configuration {

    /**
     * @param property name of the property
     * @return property value
     */
    default String getProperty(final String property) {
        return getProperties().getProperty(property);
    }

    /**
     * @return all configured {@link Properties}
     */
    Properties getProperties();

    /**
     * @param property     name of the property
     * @param defaultValue value to return if no value is present
     * @return property value
     */
    default String getProperty(String property, String defaultValue) {
        return getProperties().getProperty(property, defaultValue);
    }
}
