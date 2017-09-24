package com.netshell.libraries.properties;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Abhishek
 * @since 5/22/2016.
 */
public class InputStreamConfigurationInitializerImpl extends ConfigurationInitializerImpl {
    /**
     * {@link InputStream} which will be used to read configuration
     */
    private InputStream inputStream;

    /**
     * @param inputStream used to read configuration
     */
    public InputStreamConfigurationInitializerImpl(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * read configuration
     */
    @Override
    public void readConfig() {
        try (InputStream i = inputStream) {
            getProperties().load(i);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
