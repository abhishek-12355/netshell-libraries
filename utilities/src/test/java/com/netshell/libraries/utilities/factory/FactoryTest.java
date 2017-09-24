package com.netshell.libraries.utilities.factory;

import com.netshell.libraries.utilities.factory.custom.AbstractCustomFactory;
import com.netshell.libraries.utilities.factory.exception.FactoryException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Abhishek
 * Created on 8/30/2015.
 */
public class FactoryTest {

    private static final Logger logger = LoggerFactory.getLogger(FactoryTest.class);
    private static IFactory factory;

    @BeforeAll
    public static void setup() {

        factory = Factory.createFactory(getInitMap());
        factory.registerFactory(String.class, new AbstractCustomFactory<String>() {
            @Override
            protected String createNew(Class<?>[] parameterTypes, Object[] params) throws FactoryException {
                logger.debug("CustomFactory");
                return (String) params[0];
            }
        });
    }

    private static Map<String, String> getInitMap() {
        Map<String, String> map = new HashMap<>();
        map.put("java.lang.CharSequence", "java.lang.String");
        map.put("java.util.Collection", "java.util.List");
        map.put("java.util.List", "java.util.ArrayList");
        map.put("java.util.ArrayList", "java.lang.String");
        return map;
    }

    @Test
    public void testCreateFactory() {
        final CharSequence charSequence = factory.create(CharSequence.class, "new String");
        assertEquals(String.class, charSequence.getClass(), "Instance Test Failed");
        assertEquals("new String", charSequence, "Value Test Failed");
    }

    @Test
    public void testCreateFactoryInterface() {
        final Set charSequence = factory.create(Set.class);
        assertTrue(Proxy.isProxyClass(charSequence.getClass()), "Interface Test");
    }

    @Test
    public void testCreateFactoryInterface_fail() {
        try {
            Factory.createFactory(getInitMap(), false).create(Set.class);
        } catch (FactoryException e) {
            assertEquals("Not a Concrete type: java.util.Set", e.getMessage());
            return;
        }

        fail("Should have failed but passed");
    }

    @Test
    public void testCreateFactory_fail() {
        try {
            factory.create(Collection.class);
        } catch (FactoryException e) {
            assertEquals("java.lang.String cannot be cast to java.util.Collection", e.getMessage());
            return;
        }

        fail("Should have failed but passed");
    }

    @Test
    public void testCreateFactory_noMap() {
        final File charSequence = factory.create(File.class, "log4j2.xml");
        assertEquals(File.class, charSequence.getClass(), "Instance Test Failed");
    }

    @Test
    public void testCreateFactory_noSubstitute() {
        final ArrayList<?> charSequence = Factory.createFactory(null).create(ArrayList.class);
        assertEquals(ArrayList.class, charSequence.getClass(), "Instance Test Failed");
    }
}