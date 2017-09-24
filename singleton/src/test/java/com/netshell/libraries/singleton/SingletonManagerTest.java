package com.netshell.libraries.singleton;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Abhishek on 22-04-2015.
 */
public class SingletonManagerTest {

    private SingletonManager singletonManager;

    @BeforeEach
    public void before() {
        singletonManager = SingletonManager.createSingletonManager();
    }


    @Test
    public void testGetSingleton_Name() throws Exception {
        SingletonManagerTest test = new SingletonManagerTest();
        singletonManager.registerSingleton("SMTest", test);
        assert singletonManager.getSingleton("SMTest", SingletonManagerTest.class).get() == test;
    }

    @Test
    public void testGetSingleton_Object() throws Exception {
        SingletonManagerTest test = new SingletonManagerTest();
        singletonManager.registerSingleton(test);
        assert singletonManager.getSingleton(SingletonManagerTest.class).get() == test;
    }

    @Test
    public void testGetSingleton_Supplier() throws Exception {
        singletonManager.registerSingleton("SupplierSingletonTest", SingletonManagerTest::new);
        assert singletonManager.getSingleton("SupplierSingletonTest", SingletonManagerTest.class).get() != null;
    }


    @Test
    public void testUnregisterSingleton_Name() throws Exception {

    }

    @Test
    public void testUnregisterSingleton_Object() throws Exception {

    }
}