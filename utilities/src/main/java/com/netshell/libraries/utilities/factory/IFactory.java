package com.netshell.libraries.utilities.factory;

import com.netshell.libraries.utilities.factory.custom.CustomFactory;

/**
 * Created by Abhishek
 * on 10/29/2016.
 */
public interface IFactory {
    <T> T create(Class<T> tClass, Object... params);

    <T> void registerFactory(Class<T> tClass, CustomFactory<T> tCustomFactory);

    <T> void unregisterFactory(Class<T> tClass);
}
