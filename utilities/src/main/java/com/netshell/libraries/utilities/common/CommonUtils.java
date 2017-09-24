package com.netshell.libraries.utilities.common;

import com.netshell.libraries.singleton.SingletonManager;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * @author Abhishek
 * @since 6/5/2016.
 */
public final class CommonUtils {
    /**
     * Common Singleton Manager
     */
    private static final SingletonManager manager = SingletonManager.createSingletonManager();

    private CommonUtils() {
    }

    /**
     * @return the instance of {@link SingletonManager}
     */
    public static SingletonManager getManager() {
        return manager;
    }

    /**
     * Service Loader
     *
     * @param tClass class of service to load
     * @param <T>    type of service
     * @return an {@link Optional} having the instance og service.
     * @see ServiceLoader
     */
    public static <T> Optional<T> load(final Class<T> tClass) {
        final Iterator<T> iterator = loadList(tClass);
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }

    /**
     * Service Iterator
     *
     * @param tClass class of service to load
     * @param <T>    type of service
     * @return an {@link Iterator} of services
     * @see ServiceLoader
     */
    public static <T> Iterator<T> loadList(final Class<T> tClass) {
        return ServiceLoader.load(tClass).iterator();
    }

}
