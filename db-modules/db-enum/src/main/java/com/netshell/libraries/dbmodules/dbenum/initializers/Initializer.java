package com.netshell.libraries.dbmodules.dbenum.initializers;

/**
 * Created by Abhishek
 * on 6/11/2016.
 */
@FunctionalInterface
public interface Initializer<T, I> {
    T initialize(int index, I row) throws Exception;
}
