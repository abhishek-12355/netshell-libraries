package com.netshell.libraries.utilities.services.encoder;

/**
 * Created by Abhishek
 * on 6/12/2016.
 */
public interface Encoder {
    static Encoder defaultService() {
        return new Encoder() {
            @Override
            public byte[] encode(byte[] bytes) {
                return bytes;
            }

            @Override
            public byte[] decode(byte[] bytes) {
                return bytes;
            }

            @Override
            public String encode(String encodeString) {
                return encodeString;
            }

            @Override
            public String decode(String decodeString) {
                return decodeString;
            }
        };
    }

    static Encoder base64Encoder() {
        return new Base64Encoder();
    }

    byte[] encode(byte[] bytes);

    byte[] decode(byte[] bytes);

    String encode(String encodeString);

    String decode(String decodeString);
}
