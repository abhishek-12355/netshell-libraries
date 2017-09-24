package com.netshell.libraries.utilities.services.serializer;

import com.netshell.libraries.utilities.common.IOUtils;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Abhishek
 * on 7/2/2016.
 */
class ClassSerializer<T extends Serializable> implements Serializer<T> {

    @Override
    public byte[] serialize(final T serializable) {
        try {
            return IOUtils.toObjectBytes(serializable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(final byte[] bytes, final Class<T> tClass) {
        try {
            return IOUtils.toBytesObject(bytes, tClass);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
