package com.netshell.libraries.utilities.common;

/**
 * @author Abhishek
 * @since 06-04-2017.
 */
public final class Wrapper {
    public static <E extends Throwable> void wrapConsumer(InternalConsumer<E> consumer) {
        consumer.accept(null);
    }

    public static <R, E extends Throwable> R wrapFunction(InternalFunction<R, E> function) {
        return function.apply(null);
    }

    @FunctionalInterface
    public interface InternalFunction<R, E extends Throwable> extends java.util.function.Function<Void, R> {
        @Override
        default R apply(Void v) {
            try {
                return tryApply();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        R tryApply() throws E;
    }

    @FunctionalInterface
    public interface InternalConsumer<E extends Throwable> extends java.util.function.Consumer<Void> {
        @Override
        default void accept(Void v) {
            try {
                tryApply();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        void tryApply() throws E;
    }
}
