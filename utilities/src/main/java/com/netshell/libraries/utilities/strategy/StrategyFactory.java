package com.netshell.libraries.utilities.strategy;

import com.netshell.libraries.utilities.factory.custom.CustomFactory;

public interface StrategyFactory<T extends Strategy> extends CustomFactory<T> {
    /**
     * @return the class whose instance this factory will return.
     */
    Class<T> getStrategyClass();
}
