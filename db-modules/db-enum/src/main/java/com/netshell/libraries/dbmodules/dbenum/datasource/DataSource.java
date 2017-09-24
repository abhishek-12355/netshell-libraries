package com.netshell.libraries.dbmodules.dbenum.datasource;

import java.util.Iterator;

/**
 * @author Abhishek
 * Created on 12/13/2015.
 */
public interface DataSource<T> extends Iterator<T>, AutoCloseable {
    default void activate() {
    }
}
