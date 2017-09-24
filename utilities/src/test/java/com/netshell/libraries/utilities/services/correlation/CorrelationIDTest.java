package com.netshell.libraries.utilities.services.correlation;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CorrelationIDTest {

    @Test
    void correlationID_test() throws IOException {
        final CorrelationID id = new CorrelationID();
        id.addToken(CorrelationToken.from("ABC", "123"));
        final String idString = id.toString();
        final CorrelationID correlationID = CorrelationID.valueOf(idString);
        assertEquals(id, correlationID);
        for (CorrelationToken token : correlationID.getTokens()) {
            System.out.println(String.format("name: %s, value: %s", token.getName(), token.getValue()));
        }
    }

}