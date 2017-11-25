package com.netshell.libraries.utilities.strategy;

import com.netshell.libraries.utilities.common.CommonUtils;
import com.netshell.libraries.utilities.factory.custom.AbstractCustomFactory;

import java.util.function.Supplier;

/**
 * Created by Abhishek
 * on 10/16/2016.
 */
public abstract class AbstractStrategyFactory<T extends Strategy>
        extends AbstractCustomFactory<T>
        implements StrategyFactory<T> {

    private static final String STRATEGY_MANAGER = "STRATEGY_MANAGER";

    protected <O extends Strategy> void registerStrategy(final String name, final O strategy) {
        CommonUtils.getManager(STRATEGY_MANAGER).registerSingleton(name, strategy);
    }

    protected <O extends Strategy> O getStrategy(final Class<O> tClass, final String name) {
        return StrategyProvider.find(tClass, name);
    }

    @Override
    protected final T createNew(Class<?>[] parameterTypes, Object[] params) {
        return CommonUtils.getManager(STRATEGY_MANAGER).getSingleton((String) params[0], getType()).orElseGet(getDefault());
    }

    protected abstract Class<T> getType();

    protected abstract Supplier<T> getDefault();
}
