package com.netshell.libraries.properties;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

/**
 * @author Abhishek
 * @since 5/12/2016.
 * <p>
 * Provides mechanism to load Configuration
 */
public final class Properties {

    /**
     * Singleton. Must never have an object
     */
    private Properties() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param tClass     type of configuration, must have a default public constructor.
     * @param configFile file to load properties from
     * @param <T>        Any type extending from AbstractConfiguration.
     * @return Loaded Configuration
     */
    public static <T extends AbstractConfiguration> T createConfiguration(Class<T> tClass, String configFile) {
        return createConfiguration(tClass, Properties.class.getClassLoader().getResourceAsStream(configFile));
    }

    /**
     * @param tClass     type of configuration, must have a default public constructor.
     * @param configFile file to load properties from
     * @param <T>        Any type extending from AbstractConfiguration.
     * @return Loaded Configuration
     */
    public static <T extends AbstractConfiguration> T createConfiguration(Class<T> tClass, InputStream configFile) {
        return createConfiguration(tClass, new InputStreamConfigurationInitializerImpl(configFile));
    }

    /**
     * @param tClass      type of configuration, must have a default public constructor.
     * @param initializer Initializer that will be used to read configuration.
     * @param <T>         Any type extending from AbstractConfiguration.
     * @return Loaded Configuration
     */
    public static <T extends AbstractConfiguration> T createConfiguration(Class<T> tClass, ConfigurationInitializer initializer) {
        try {
            final Constructor<T> tConstructor = tClass.getDeclaredConstructor();
            tConstructor.setAccessible(true);
            final T t = tConstructor.newInstance();
            t.getProperties().putAll(initializer.loadConfig());
            t.parseConfiguration();
            return t;
        } catch (InstantiationException |
                IllegalAccessException |
                NoSuchMethodException |
                InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param tClass     type of configuration, must have a default public constructor.
     * @param configFile file to load properties from
     * @param <T>        Any type extending from AbstractConfiguration.
     * @return Loaded Configuration
     */
    public static <T extends AbstractConfiguration> T createConfiguration(Class<T> tClass, File configFile) {
        try {
            return createConfiguration(tClass, new FileInputStream(configFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param tClass     type of configuration, must have a default public constructor.
     * @param configFile file to load properties from
     * @param <T>        Any type extending from AbstractConfiguration.
     * @return Loaded Configuration
     */
    public static <T extends AbstractConfiguration> T createConfiguration(Class<T> tClass, final URI configFile) {
        try {
            return createConfiguration(tClass, configFile.toURL().openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
