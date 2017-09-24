package com.netshell.libraries.utilities.services.id;

import java.util.UUID;

/**
 * Created by Abhishek
 * on 6/12/2016.
 */
@FunctionalInterface
public interface IDGenerator {
    static IDGenerator defaultService() {
        return () -> UUID.randomUUID().toString();
    }

    String generateId();

    default String generateId(final String generatorCriteria) {
        return generateId();
    }
}
