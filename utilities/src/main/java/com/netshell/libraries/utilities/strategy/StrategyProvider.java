package com.netshell.libraries.utilities.strategy;

import com.netshell.libraries.utilities.common.CommonUtils;
import com.netshell.libraries.utilities.factory.Factory;
import com.netshell.libraries.utilities.factory.IFactory;
import com.netshell.libraries.utilities.factory.custom.CustomFactory;

/**
 * Created by Abhishek
 * on 5/22/2016.
 */
@SuppressWarnings("unchecked")
public final class StrategyProvider {

    private static final IFactory factory;

    static {
        factory = Factory.createFactory(null);

        CommonUtils.loadList(StrategyFactory.class)
                .forEachRemaining(strategyFactory -> addProvider(strategyFactory.getStrategyClass(), strategyFactory));
    }

    private StrategyProvider() {

    }

    public static <T extends Strategy> T find(Class<T> tClass, String strategyName) {
        return factory.create(tClass, strategyName);
    }

    public static <T extends Strategy, F extends CustomFactory<T>> void addProvider(final Class<T> tClass, F customFactory) {
        factory.registerFactory(tClass, customFactory);
    }
}
