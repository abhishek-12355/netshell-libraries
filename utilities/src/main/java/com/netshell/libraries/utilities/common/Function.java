package com.netshell.libraries.utilities.common;

/**
 * @author Abhishek
 * @since 10/15/2016.
 */
@FunctionalInterface
public interface Function<T, R, E extends Throwable> extends java.util.function.Function<T, R> {
    @Override
    default R apply(T t) {
        try {
            return tryApply(t);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    R tryApply(T t) throws E;
}
