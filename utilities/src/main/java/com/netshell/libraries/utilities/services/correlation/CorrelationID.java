package com.netshell.libraries.utilities.services.correlation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.netshell.libraries.utilities.common.IOUtils;
import com.netshell.libraries.utilities.common.JsonUtils;
import com.netshell.libraries.utilities.sequencer.SequencerProcess;
import com.netshell.libraries.utilities.services.encoder.EncoderService;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.netshell.libraries.utilities.common.Wrapper.wrapFunction;

public final class CorrelationID implements Serializable {

    private final Map<String, CorrelationToken> tokenMap = new HashMap<>();

    public static boolean isValid(CorrelationID correlationID) {
        return false;
    }

    public static CorrelationID valueOf(String correlation) throws IOException {
        final String stringImpl = SequencerProcess
                .createProcess(v -> correlation.getBytes())
                .andThan(bytes -> wrapFunction(() -> IOUtils.decompress(bytes)))
                .andThan(EncoderService::decode)
//                .andThan(EncryptionService::decrypt)
                .andThan((Function<byte[], String>) String::new).execute();

        final CorrelationID correlationID = new CorrelationID();
        correlationID.tokenMap.putAll(fromStringImpl(stringImpl));
        return correlationID;
    }

    private static Map<String, CorrelationToken> fromStringImpl(String stringImpl) throws IOException {
        return JsonUtils.readValue(stringImpl, new TypeReference<Map<String, CorrelationToken>>() {
        });
    }

    public Iterable<CorrelationToken> getTokens() {
        return tokenMap.values();
    }

    public CorrelationToken findToken(String name) {
        return tokenMap.get(name);
    }

    public void addToken(CorrelationToken token) {
        if (tokenMap.containsKey(token.getName())) {
            throw new IllegalArgumentException(String.format("token with name %s already exists", token.getName()));
        }

        tokenMap.put(token.getName(), token);
    }

    public void removeToken(String name) {
        tokenMap.remove(name);
    }

    @Override
    public String toString() {
        // TODO: find a way to preserve tokens beyond multiple instances / encoding services

        return SequencerProcess
                .createProcess(v -> wrapFunction(() -> toStringImpl().getBytes()))
//                .andThan(EncryptionService::encrypt)
                .andThan(EncoderService::encode)
                .andThan(bytes -> wrapFunction(() -> IOUtils.compress(bytes)))
                .andThan((Function<byte[], String>) String::new).execute();
    }

    private String toStringImpl() throws IOException {
        return JsonUtils.writeValueAsString(tokenMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CorrelationID that = (CorrelationID) o;

        return tokenMap.equals(that.tokenMap);
    }

    @Override
    public int hashCode() {
        return tokenMap.hashCode();
    }
}
