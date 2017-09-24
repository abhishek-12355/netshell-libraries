package com.netshell.libraries.properties;

/**
 * @author Abhishek
 * @since 5/22/2016.
 */
public abstract class ConfigurationInitializerImpl implements ConfigurationInitializer {
    /**
     * read properties.
     */
    private java.util.Properties properties = new java.util.Properties();

    /**
     * @return {@link java.util.Properties} instance of all the properties so far read.
     */
    @Override
    public java.util.Properties getProperties() {
        return properties;
    }
}
