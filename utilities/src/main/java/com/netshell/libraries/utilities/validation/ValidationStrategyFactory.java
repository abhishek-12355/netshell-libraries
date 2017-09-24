package com.netshell.libraries.utilities.validation;

import com.netshell.libraries.utilities.common.CommonUtils;
import com.netshell.libraries.utilities.strategy.AbstractStrategyFactory;

import java.util.function.Supplier;

/**
 * Created by Abhishek
 * on 5/27/2016.
 */
public class ValidationStrategyFactory extends AbstractStrategyFactory<ValidationStrategy> {

    public ValidationStrategyFactory() {
        CommonUtils.loadList(ValidationStrategy.class)
                .forEachRemaining(validationStrategy -> addType(validationStrategy.getName(), validationStrategy));
    }

    public <T extends ValidationStrategy> void addType(final String name, final T strategy) {
        registerStrategy(name, strategy);
    }

    @SuppressWarnings("unchecked")
    public ValidationStrategy getStrategy(final String name) {
        return getStrategy(ValidationStrategy.class, name);
    }

    @Override
    protected Class<ValidationStrategy> getType() {
        return ValidationStrategy.class;
    }

    @Override
    protected Supplier<ValidationStrategy> getDefault() {
        return ValidationStrategy::identity;
    }

    @Override
    public Class<ValidationStrategy> getStrategyClass() {
        return ValidationStrategy.class;
    }
}
