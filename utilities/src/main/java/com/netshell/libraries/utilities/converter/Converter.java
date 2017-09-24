package com.netshell.libraries.utilities.converter;

/**
 * @author Abhishek
 * @since 26-06-2015.
 */
public interface Converter<F, T> {
    default T convert(F f) {
        throw new UnsupportedOperationException("convert is not supported");
    }

    default F convertBack(T t) {
        throw new UnsupportedOperationException("convert back is not supported");
    }
}
