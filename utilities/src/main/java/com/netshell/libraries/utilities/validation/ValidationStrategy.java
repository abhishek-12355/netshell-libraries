package com.netshell.libraries.utilities.validation;

import com.netshell.libraries.utilities.strategy.Strategy;

/**
 * Created by Abhishek
 * on 5/22/2016.
 */
public interface ValidationStrategy<T> extends Strategy {
    /**
     * Does this lambda creates ValidationStrategy with each call?
     *
     * @param <T>
     * @return
     */
    static <T> ValidationStrategy<T> identity() {
        return new ValidationStrategy<T>() {
            @Override
            public T validate(T t) {
                return t;
            }

            @Override
            public String getName() {
                return "ValidationStrategy.identity";
            }
        };
    }

    T validate(T t) throws ValidationException;

}
