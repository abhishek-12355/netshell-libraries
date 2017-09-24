package com.netshell.libraries.properties;

import java.util.Properties;

/**
 * @author Abhishek
 * @since 5/13/2016.
 * <p>
 * A base class or all Application Configuration
 */
public abstract class AbstractConfiguration implements Configuration {

    private final Properties properties = new Properties();

    /**
     * Returns the current configuration settings.
     *
     * @return {@link Properties}
     */
    @Override
    public Properties getProperties() {
        return properties;
    }

    /**
     * A hook that can be used by implementers to do customizations.
     */
    protected void parseConfiguration() {

    }
}
