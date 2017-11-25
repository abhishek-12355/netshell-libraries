package com.netshell.libraries.utilities.common;

import com.netshell.libraries.singleton.SingletonManager;

import java.util.*;

/**
 * @author Abhishek
 * @since 6/5/2016.
 */
public final class CommonUtils {
    private static final String DEFAULT_MANAGER = "DEFAULT_MANAGER";

    /**
     * Common Singleton Manager
     */
    private static final Map<String, SingletonManager> managerMap = new HashMap<>();

    private CommonUtils() {
    }

    /**
     * @return the instance of {@link SingletonManager}
     */
    public static SingletonManager getManager() {
        return getManager(DEFAULT_MANAGER);
    }

    public static SingletonManager getManager(final String name) {
        SingletonManager singletonManager = managerMap.get(name);
        if (singletonManager == null) {
            singletonManager = SingletonManager.createSingletonManager();
            managerMap.put(name, singletonManager);
        }
        return singletonManager;
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
