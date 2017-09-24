package com.netshell.libraries.utilities.factory.resolver;

/**
 * @author Abhishek
 * Created on 8/23/2015.
 */
@FunctionalInterface
public interface Resolver<I, O> {

    /**
     * Consume input to produce O
     *
     * @param input used to compute the return
     * @return O
     */
    O resolve(I input);
}
