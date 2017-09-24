package com.netshell.libraries.utilities.services.encoder;

import java.util.Base64;

/**
 * Created by Abhishek
 * on 7/2/2016.
 */
final class Base64Encoder implements Encoder {
    @Override
    public byte[] encode(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    @Override
    public String encode(String encodeString) {
        return Base64.getEncoder().encodeToString(encodeString.getBytes());
    }

    @Override
    public String decode(String decodeString) {
        return new String(Base64.getDecoder().decode(decodeString));
    }
}
