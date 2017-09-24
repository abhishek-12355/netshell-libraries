package com.netshell.libraries.utilities.services.serializer;

import com.netshell.libraries.utilities.common.JsonUtils;

import java.io.IOException;
import java.io.Serializable;

class JsonSerializer<T extends Serializable> implements Serializer<T> {
    @Override
    public byte[] serialize(final T serializable) {
        try {
            return JsonUtils.writeValueAsBytes(serializable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(final byte[] bytes, final Class<T> tClass) {
        try {
            return JsonUtils.readValue(bytes, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
