package com.netshell.libraries.utilities.services.encoder;

import com.netshell.libraries.utilities.common.CommonUtils;

/**
 * Created by Abhishek
 * on 6/12/2016.
 */
public final class EncoderService {
    private static final Encoder ENCODER;

    static {
        ENCODER = CommonUtils.load(Encoder.class).orElseGet(Encoder::defaultService);
    }

    private EncoderService() {
    }

    public static byte[] encode(byte[] bytes) {
        return ENCODER.encode(bytes);
    }

    public static byte[] decode(byte[] bytes) {
        return ENCODER.decode(bytes);
    }

    public static String encode(String encodeString) {
        return ENCODER.encode(encodeString);
    }

    public static String decode(String decodeString) {
        return ENCODER.decode(decodeString);
    }
}
