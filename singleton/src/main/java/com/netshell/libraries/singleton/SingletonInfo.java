package com.netshell.libraries.singleton;

import java.util.Calendar;
import java.util.Date;
import java.util.function.Supplier;

/**
 * Stores the singleton object and its related info
 *
 * @author Abhishek
 * @since 1/9/2016.
 */
final class SingletonInfo {
    /**
     * lazy load object
     */
    private final Supplier<Object> supplier;
    /**
     * The singleton object
     */
    private Object singleton;
    /**
     * Keep track of last access of the singleton object
     */
    private Date date;

    /**
     * Creates a singleton object
     *
     * @param singleton object.
     */
    SingletonInfo(final Object singleton) {
        this(singleton, null);
    }

    private SingletonInfo(Object singleton, Supplier<Object> supplier) {
        this.singleton = singleton;
        this.supplier = supplier;
        this.date = Calendar.getInstance().getTime();
    }

    SingletonInfo(Supplier<Object> supplier) {
        this(null, supplier);
    }

    /**
     * @return last access date and time of this object
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return reset time and return the object
     */
    Object getSingleton() {
        date = Calendar.getInstance().getTime();
        if (singleton == null && supplier != null) singleton = supplier.get();
        return singleton;
    }

    /**
     * Remove object.
     */
    public void remove() {
        singleton = null;
    }

    /**
     * @return this object without resetting time.
     */
    public Object peekSingleton() {
        return singleton;
    }
}
