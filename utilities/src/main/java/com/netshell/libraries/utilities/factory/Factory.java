package com.netshell.libraries.utilities.factory;

import com.netshell.libraries.utilities.common.CommonMethods;
import com.netshell.libraries.utilities.common.ReflectionUtils;
import com.netshell.libraries.utilities.factory.custom.CustomFactory;
import com.netshell.libraries.utilities.factory.exception.FactoryException;
import com.netshell.libraries.utilities.factory.resolver.ClassResolver;
import com.netshell.libraries.utilities.factory.resolver.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhishek
 * Created on 8/23/2015.
 */
public final class Factory implements IFactory {

    private static final Logger logger = LoggerFactory.getLogger(Factory.class);

    /**
     * Factory delegate.
     */
    private final IFactory delegate;

    /**
     * constructor to create a factory.
     *
     * @param initialMap          initial Factory Substitution settings
     * @param initializeAbstracts true if Abstract classes and Interfaces should be initialized with proxy class
     */
    private Factory(final Map<String, String> initialMap, final boolean initializeAbstracts) {
        this.delegate = new FactoryInternal(initialMap, initializeAbstracts);
    }

    /**
     * @param initialMap          initial Factory Substitution settings
     * @param initializeAbstracts true if Abstract classes and Interfaces should be initialized with proxy class
     * @return a new Factory with initialMap substitution settings
     */
    public static Factory createFactory(
            final Map<String, String> initialMap,
            final boolean initializeAbstracts) {
        return new Factory(initialMap, initializeAbstracts);
    }

    /**
     * Create a Factory Instance
     *
     * @param initialMap initial Factory Substitution settings
     * @return a new Factory with initialMap substitution settings
     */
    public static Factory createFactory(final Map<String, String> initialMap) {
        return createFactory(initialMap, true);
    }

    /**
     * @param tClass Class of type of object to be created
     * @param params parameters to be used to create object
     * @param <T>    type of object to be created.
     * @return an instance of object
     */
    @Override
    public <T> T create(final Class<T> tClass, final Object... params) {
        return delegate.create(tClass, params);
    }

    /**
     * @param tClass         against which a custom factory is to be registered
     * @param tCustomFactory custom factory to register
     * @param <T>            type
     */
    @Override
    public <T> void registerFactory(final Class<T> tClass, final CustomFactory<T> tCustomFactory) {
        delegate.registerFactory(tClass, tCustomFactory);
    }

    /**
     * remove custom factory.
     *
     * @param tClass custom factory class to be removed.
     * @param <T>    type
     */
    @Override
    public <T> void unregisterFactory(final Class<T> tClass) {
        delegate.unregisterFactory(tClass);
    }

    private final static class FactoryInternal implements IFactory {

        /**
         * Resolve the substitution of class
         */
        private final ClassResolver classResolver = new ClassResolver();
        /**
         * find types of Parameters
         */
        private final ParameterResolver parameterTypeResolver = new ParameterResolver();
        /**
         * Register Custom Factories
         */
        private final Map<String, CustomFactory<?>> customFactoryMap = new HashMap<>();

        /**
         * Instantiate Abstract and Interface types
         */
        private final boolean initializeAbstracts;

        /**
         * create a Factory
         *
         * @param initialMap          initial Factory Substitution settings
         * @param initializeAbstracts indicate whether to initialize abstract types with {@link ReflectionUtils#createIdentityProxy(Class)}
         */
        FactoryInternal(final Map<String, String> initialMap, final boolean initializeAbstracts) {
            this.initializeAbstracts = initializeAbstracts;
            classResolver.initialize(initialMap);
        }

        /**
         * Create an object of the type <code>tClass</code>.
         * If a custom factory is provided, it is used.
         * If the substituted class is abstract or interface, proxy object {@link ReflectionUtils#createIdentityProxy(Class)} is returned.
         * Otherwise a new object is returned
         *
         * @param tClass Class of type of object to be created
         * @param params parameters to be used to create object
         * @param <T>    type of object to be created.
         * @return an instance of object
         */
        @Override
        @SuppressWarnings("unchecked")
        public <T> T create(final Class<T> tClass, final Object... params) {
            logger.debug("Initialising Class: " + tClass.getName());
            try {
                Class<? extends T> tSubstitutedClass = tClass;
                if (this.classResolver.canResolve()) {
                    final Class<?> aClass = Class.forName(classResolver.resolve(tSubstitutedClass.getName()));
                    logger.trace("Substituted Class: {} -> {}", tClass.getName(), aClass.getName());
                    if (!tClass.isAssignableFrom(aClass)) {
                        throw new FactoryException(String.format("%s cannot be cast to %s", aClass.getName(), tClass.getName()));
                    }

                    tSubstitutedClass = (Class<? extends T>) aClass;
                }

                if (customFactoryMap.containsKey(tSubstitutedClass.getName())) {
                    return ((CustomFactory<T>) customFactoryMap.get(tSubstitutedClass.getName())).create(params);
                }

                if (tSubstitutedClass.isInterface() || Modifier.isAbstract(tSubstitutedClass.getModifiers())) {
                    if (initializeAbstracts) {
                        return ReflectionUtils.createIdentityProxy(tSubstitutedClass);
                    }
                    throw new FactoryException("Not a Concrete type: " + tClass.getName());
                }

                return tSubstitutedClass.getConstructor(parameterTypeResolver.resolve(params)).newInstance(params);
            } catch (FactoryException e) {
                throw e;
            } catch (final Exception e) {
                throw new FactoryException("Unable To instantiate: " + tClass.getName(), e);
            }
        }

        /**
         * @param tClass         against which a custom factory is to be registered
         * @param tCustomFactory custom factory to register
         * @param <T>            type
         */
        @Override
        public <T> void registerFactory(final Class<T> tClass, final CustomFactory<T> tCustomFactory) {
            CommonMethods.checkInput(tClass);
            CommonMethods.checkInput(tCustomFactory);
            if (this.customFactoryMap.containsKey(tClass.getName())) {
                throw new FactoryException("Already Registered Factory: " + tClass.getName());
            }

            logger.trace("Registering custom factory {} -> {}", tClass.getName(), tCustomFactory.getClass().getName());
            this.customFactoryMap.put(tClass.getName(), tCustomFactory);
        }

        /**
         * remove custom factory.
         *
         * @param tClass custom factory class to be removed.
         * @param <T>    type
         */
        @Override
        public <T> void unregisterFactory(final Class<T> tClass) {
            CommonMethods.checkInput(tClass);
            logger.trace("Removing custom factory {}", tClass.getName());
            this.customFactoryMap.remove(tClass.getName());
        }
    }
}
