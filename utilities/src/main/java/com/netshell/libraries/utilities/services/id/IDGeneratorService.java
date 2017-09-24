package com.netshell.libraries.utilities.services.id;

import com.netshell.libraries.utilities.common.CommonUtils;

/**
 * Created by Abhishek
 * on 6/12/2016.
 */
public final class IDGeneratorService {
    private static final IDGenerator idGenerator;

    static {
        idGenerator = CommonUtils.load(IDGenerator.class).orElseGet(IDGenerator::defaultService);
    }

    private IDGeneratorService() {

    }

    public static synchronized String create() {
        return idGenerator.generateId();
    }

    public static synchronized String create(final String criteria) {
        return idGenerator.generateId(criteria);
    }
}
