package com.netshell.libraries.dbmodules.dbenum.datasource;

import java.util.Iterator;

/**
 * @author Abhishek
 * Created on 12/26/2015.
 */
public class IterableDataSource<T> implements DataSource<T> {

    private final Iterator<T> tIterator;

    public IterableDataSource(Iterable<T> tIterable) {
        this.tIterator = tIterable.iterator();
    }

    public IterableDataSource(Iterator<T> tIterator) {
        this.tIterator = tIterator;
    }

    @Override
    public void close() {
        // no action
    }

    @Override
    public boolean hasNext() {
        return this.tIterator.hasNext();
    }

    @Override
    public T next() {
        return this.tIterator.next();
    }
}
