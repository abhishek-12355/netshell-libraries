package com.netshell.libraries.utilities.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ashekha
 * @since 10/29/2016.
 */
public final class CommonMethods {

    /**
     * List of primitive types
     */
    private static final List<Class<?>> LEAVES = Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class,
            String.class);

    private CommonMethods() {
    }

    /**
     * @param input
     * @param <T>
     * @return
     */
    public static <T> T checkInput(final T input) {
        return checkInput(input, "Input Validation Failed");
    }

    /**
     * @param input
     * @param failMessage
     * @param <T>
     * @return
     */
    public static <T> T checkInput(final T input, final String failMessage) {
        if (!validateInput(input)) {
            throwException(failMessage);
        }

        return input;
    }

    private static <T> boolean validateInput(T input) {
        return input == null ||
                !(input instanceof CharSequence && isEmpty((CharSequence) input)) ||
                !(input instanceof Collection && isEmpty((Collection) input)) ||
                !(input instanceof Map && isEmpty((Map) input)) ||
                !(input.getClass().isArray() && isEmpty((Object[]) input));
    }

    private static <T> void throwException(final String failMessage) {
        throw new RuntimeException(failMessage);
    }

    public static boolean isEmpty(final CharSequence value) {
        return value == null || "".equals(value);
    }

    public static <T> boolean isEmpty(final Collection<T> tCollection) {
        return tCollection == null || tCollection.isEmpty();
    }

    public static <S, T> boolean isEmpty(final Map<S, T> stMap) {
        return stMap == null || stMap.isEmpty();
    }

    public static <T> boolean isEmpty(final T[] tArray) {
        return tArray == null || tArray.length == 0;
    }

    public static <T> T checkInput(final T input, final Supplier<String> failMessage) {
        if (!validateInput(input)) {
            throwException(failMessage.get());
        }

        return input;
    }

    public static void ifNotEmpty(final CharSequence value, final Consumer<CharSequence> consumer) {
        if (!CommonMethods.isEmpty(value)) {
            consumer.accept(value);
        }
    }

    public static <S, T extends Collection<S>> void ifNotEmpty(final T tCollection, final Consumer<T> consumer) {
        if (!CommonMethods.isEmpty(tCollection)) {
            consumer.accept(tCollection);
        }
    }

    public static <T> void ifNotEmpty(final T[] tArray, final Consumer<T[]> consumer) {
        if (!CommonMethods.isEmpty(tArray)) {
            consumer.accept(tArray);
        }
    }

    /**
     * @param stMap
     * @param consumer
     * @param <R>
     * @param <S>
     * @param <T>
     */
    public static <R, S, T extends Map<R, S>> void ifNotEmpty(final T stMap, final Consumer<T> consumer) {
        if (!CommonMethods.isEmpty(stMap)) {
            consumer.accept(stMap);
        }
    }

    /**
     * dumps the throwable object into a {@link String}
     *
     * @param throwable to be dumped
     * @return a {@link String} containg the dump the of {@code throwable} object
     */
    public static String toString(final Throwable throwable) {
        final StringWriter writer = new StringWriter();
        try (final PrintWriter printWriter = new PrintWriter(writer)) {
            throwable.printStackTrace(printWriter);
            printWriter.flush();
        }
        return writer.toString();
    }

    /**
     * @param o
     * @return
     * @throws IllegalAccessException
     * @deprecated
     */
    @Deprecated
    public static String toObjectString(Object o) throws IllegalAccessException {
        if (o == null) {
            return "null";
        }

        if (LEAVES.contains(o.getClass())) {
            return o.toString();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getSimpleName()).append(": [");
        for (Field f : o.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            f.setAccessible(true);
            sb.append(f.getName()).append(": ");
            sb.append(toObjectString(f.get(o))).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
