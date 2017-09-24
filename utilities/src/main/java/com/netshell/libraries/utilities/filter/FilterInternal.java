package com.netshell.libraries.utilities.filter;

import com.netshell.libraries.utilities.common.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by Abhishek
 * on 5/16/2016.
 */
final class FilterInternal<F> {
    private final F filterCriteria;

    FilterInternal(F filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    private static boolean isDefault(Object obj, Class<?> type) {
        final Object o = ReflectionUtils.defaultFromPrimitive(type);
        return o != null && o.equals(obj);
    }

    @SuppressWarnings("unchecked")
    public boolean match(F item) {
        final Field[] declaredFields = item.getClass().getDeclaredFields();

        try {
            for (Field f : declaredFields) {
                f.setAccessible(true);
                final Object obj = f.get(filterCriteria);
                final Object other = f.get(item);
                if (obj != null && !isDefault(obj, f.getType()) && (
                        obj instanceof Comparable && ((Comparable) obj).compareTo(other) != 0 ||
                                !obj.equals(other)
                )) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
