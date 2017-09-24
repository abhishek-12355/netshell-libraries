package com.netshell.libraries.singleton;

import com.netshell.libraries.singleton.exceptions.SingletonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author Abhishek
 * @since 1/9/2016.
 */
public final class SingletonManager {

    /**
     * logger to be used
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonManager.class);

    /**
     * Map containing the singleton objects
     */
    private final Map<String, SingletonInfo> singletonMap;

    /**
     * create a new SingletonManager
     */
    private SingletonManager() {
        singletonMap = new ConcurrentHashMap<>(Configuration.getInstance().getInitialCapacity());
    }

    /**
     * @return create a new SingletonManager
     */
    public static SingletonManager createSingletonManager() {
        return new SingletonManager();
    }

    /**
     * Register a new Singleton Object. This attempts to lazy initialize singleton.
     *
     * @param name              of the singleton to be registered
     * @param singletonSupplier supplier for singletonObject.
     */
    public void registerSingleton(final String name, final Supplier<Object> singletonSupplier) {
        registerSingleton(name, new SingletonInfo(singletonSupplier));
    }

    /**
     * Register a new Singleton Object
     *
     * @param name          of the singleton to be registered
     * @param singletonInfo object to be registered.
     */
    private void registerSingleton(final String name, final SingletonInfo singletonInfo) {
        Objects.requireNonNull(name);

        if (isRegistered(name)) {
            throw new SingletonException("Singleton Object already registered: " + name);
        }
        this.singletonMap.put(name, singletonInfo);
        LOGGER.debug("Registered Singleton Object: {}", name);
    }

    /**
     * @param name of singleton to be checked.
     * @return true if the name is already registered
     */
    public boolean isRegistered(final String name) {
        return this.singletonMap.containsKey(name);
    }

    /**
     * @param singletonObject object to be registered
     */
    public void registerSingleton(final Object singletonObject) {
        this.registerSingleton(singletonObject.getClass().getName(), singletonObject);
    }

    /**
     * Register a new Singleton Object
     *
     * @param name            of the singleton to be registered
     * @param singletonObject object to be registered.
     */
    public void registerSingleton(final String name, final Object singletonObject) {
        registerSingleton(name, new SingletonInfo(singletonObject));
    }

    /**
     * @param tClass to check for registration
     * @return true if tClass is registered
     */
    public boolean isRegistered(final Class<?> tClass) {
        return this.isRegistered(tClass.getName());
    }

    /**
     * @param oClass class representing the type of object
     * @param <O>    type of object
     * @return an optional containing singleton object
     */
    public <O> Optional<O> getSingleton(final Class<O> oClass) {
        return this.getSingleton(oClass.getName(), oClass);
    }

    /**
     * @param name   of the singleton
     * @param oClass class of type of singleton
     * @return singleton object
     */
    @SuppressWarnings("unchecked")
    public <O> Optional<O> getSingleton(final String name, final Class<O> oClass) {
        Objects.requireNonNull(oClass);
        final SingletonInfo singletonInfo = this.singletonMap.get(name);
        return singletonInfo == null ? Optional.empty() : Optional.of(oClass.cast(singletonInfo.getSingleton()));
    }

    /**
     * Remove the associated singleton
     *
     * @param tClass class of type of object
     * @param <O>    type of object
     */
    public <O> void unregisterSingleton(final Class<O> tClass) {
        this.unregisterSingleton(tClass.getName());
    }

    /**
     * Remove the associated singleton
     *
     * @param name of singleton
     */
    public void unregisterSingleton(final String name) {
        this.singletonMap.remove(name);
        LOGGER.debug("Unregistered Singleton Object: {}", name);
    }

}

