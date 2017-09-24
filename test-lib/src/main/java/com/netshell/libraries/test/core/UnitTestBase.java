package com.netshell.libraries.test.core;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * A JUnit runner super class for all PowerMock Tests
 *
 * @author Abhishek
 * @since 10/15/2016.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*"})
public abstract class UnitTestBase {

    /**
     * Mock a class having {@code getInstance()} method.
     *
     * @param tClass class with {@code getInstance()} method
     * @param <T>    type of object
     * @return A mocked object of {@code tClass}
     */
    protected <T> T mockGetInstanceClass(final Class<T> tClass) {
        return mockGetInstanceClass(tClass, Mockito.mock(tClass));
    }

    /**
     * Mock a class having {@code getInstance()} method.
     *
     * @param tClass  class with {@code getInstance()} method
     * @param tObject object to return when {@code getInstance()} is called
     * @param <T>     type of object
     * @return {@code tObject}
     */
    protected <T> T mockGetInstanceClass(final Class<T> tClass, final T tObject) {
        PowerMockito.mockStatic(tClass);
        try {
            PowerMockito.when(tClass, "getInstance").thenReturn(tObject);
        } catch (Exception e) {
            throw new RuntimeException("Unable to mock", e);
        }
        return tObject;
    }
}
