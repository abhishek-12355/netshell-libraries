package com.netshell.libraries.utilities.filter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Abhishek
 * Created on 12/4/2015.
 */
public final class Filter {

    private Filter() {
        throw new UnsupportedOperationException();
    }

    public static <T> List<T> filter(Collection<T> tCollection, T filterCriteria) {
        final FilterInternal<T> f = new FilterInternal<>(filterCriteria);
        return tCollection.stream().filter(f::match).collect(Collectors.toList());
    }

    public static <T> Optional<T> findFirst(Collection<T> tCollection, T filterCriteria) {
        final FilterInternal<T> f = new FilterInternal<>(filterCriteria);
        return tCollection.stream().filter(f::match).findFirst();
    }

    public static <T> List<T> filter(Collection<T> tCollection, Predicate<T> filterCriteria) {
        return tCollection.stream().filter(filterCriteria).collect(Collectors.toList());
    }

    public static <T> Optional<T> findFirst(Collection<T> tCollection, Predicate<T> filterCriteria) {
        return tCollection.stream().filter(filterCriteria).findFirst();
    }
}
