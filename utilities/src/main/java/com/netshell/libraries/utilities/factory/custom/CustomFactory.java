package com.netshell.libraries.utilities.factory.custom;

/**
 * Custom Factory used to create new Objects
 *
 * @author Abhishek
 * Created on 8/31/2015.
 */
@FunctionalInterface
public interface CustomFactory<T> {
    /**
     * @param params parameter list that is used to create the object
     * @return an instance of T
     */
    T create(Object... params);
}
