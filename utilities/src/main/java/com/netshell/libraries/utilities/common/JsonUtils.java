package com.netshell.libraries.utilities.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public static <T> T readValue(String s, TypeReference<T> tTypeReference) throws IOException {
        return OBJECT_MAPPER.readValue(s, tTypeReference);
    }

    public static <T> T readValue(String s, Class<T> tClass) throws IOException {
        return OBJECT_MAPPER.readValue(s, tClass);
    }

    public static <T> T readValue(byte[] bytes, Class<T> tClass) throws IOException {
        return OBJECT_MAPPER.readValue(bytes, tClass);
    }

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static byte[] writeValueAsBytes(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }
}
