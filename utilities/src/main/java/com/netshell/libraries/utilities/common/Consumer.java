package com.netshell.libraries.utilities.common;

/**
 * @author ashekha
 * @since 11/5/2016.
 */
@FunctionalInterface
public interface Consumer<T, E extends Throwable> extends java.util.function.Consumer<T> {

    @Override
    default void accept(T t) {
        try {
            tryAccept(t);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    void tryAccept(final T t) throws E;
}
