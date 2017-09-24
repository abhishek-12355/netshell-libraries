package com.netshell.libraries.utilities.services.serializer;

import java.io.Serializable;

/**
 * Created by Abhishek
 * on 7/2/2016.
 */
public interface Serializer<T extends Serializable> {
    static <T extends Serializable> Serializer<T> classSerializer() {
        return new ClassSerializer<>();
    }

    static <T extends Serializable> Serializer<T> jsonSerializer() {
        return new JsonSerializer<>();
    }

    byte[] serialize(T serializable);

    T deserialize(byte[] bytes, Class<T> tClass);
}
