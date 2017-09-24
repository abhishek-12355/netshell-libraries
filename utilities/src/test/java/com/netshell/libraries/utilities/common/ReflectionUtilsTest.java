package com.netshell.libraries.utilities.common;


import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

/**
 * Created by ashekha on 10/29/2016.
 */
public class ReflectionUtilsTest {
    @Test
    public void createIdentityProxy() throws Exception {
        final List proxy = ReflectionUtils.createIdentityProxy(List.class);
        proxy.add(0);
        proxy.get(0);
    }

    @Test
    public void createProxy() throws Exception {
        final Object proxy = ReflectionUtils.createProxy(Set.class, CharSequence.class);
        ((Set) proxy).add("1");
        ((CharSequence) proxy).charAt(0);
    }

}