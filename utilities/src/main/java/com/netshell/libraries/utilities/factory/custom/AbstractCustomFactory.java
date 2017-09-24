package com.netshell.libraries.utilities.factory.custom;

import com.netshell.libraries.utilities.factory.resolver.ParameterResolver;

/**
 * Base class representing the custom factory.
 *
 * @author Abhishek
 * Created on 8/31/2015.
 */
public abstract class AbstractCustomFactory<T> implements CustomFactory<T> {

    /**
     * @param params parameter list that is used to create the object
     * @return return an instance if T
     */
    @Override
    public T create(Object... params) {
        return createNew(new ParameterResolver().resolve(params), params);
    }

    /**
     * @param parameterTypes
     * @param params
     * @return
     */
    protected abstract T createNew(Class<?>[] parameterTypes, Object[] params);
}
