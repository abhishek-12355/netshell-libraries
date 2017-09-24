package com.netshell.libraries.utilities.collection;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Collection Utilities
 *
 * @author Abhishek
 * @since 7/31/2016.
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Split the list and return a list containing the resultant lists.
     *
     * @param list to be split
     * @param size of each list after splitting.
     * @param <T>  type of list.
     * @return {@link List} of {@link List}s
     */
    public static <T> List<List<T>> splitList(final List<T> list, final int size) {
        return splitListStream(list, size).collect(Collectors.toList());
    }

    /**
     * Split the list and return the stream with the resultant lists.
     *
     * @param list to be split
     * @param size of each list after splitting.
     * @param <T>  type of list.
     * @return {@link Stream} of {@link List}s
     */
    public static <T> Stream<List<T>> splitListStream(final List<T> list, final int size) {

        if (size <= 0) {
            throw new IllegalArgumentException("Invalid Split Size");
        }

        final int listSize = list.size();

        if (listSize == 0) {
            return Stream.empty();
        }

        return IntStream.rangeClosed(0, (listSize - 1) / size)
                .mapToObj(n -> list.subList(n * size, Math.min((n + 1) * size, listSize)));
    }

    /**
     * Split the list and return the stream with the resultant lists.
     *
     * @param list to be split
     * @param size of each list after splitting.
     * @param <T>  type of list.
     * @return {@link List} of {@link ArrayList}s
     */
    public static <T> List<List<T>> splitArrayList(final List<T> list, final int size) {
        return splitListStream(list, size)
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    /**
     * Split the list into batches of size
     *
     * @param list to create batch from
     * @param size of each batch
     * @param <T>  type of list
     * @return {@link Stream} of batches.
     */
    public static <T> Stream<List<T>> batchStream(final List<T> list, final int size) {

        if (size <= 0) {
            throw new IllegalArgumentException("Invalid Split Size");
        }

        final int listSize = list.size();
        if (listSize == 0) {
            return Stream.empty();
        }

        return StreamSupport.stream(new BatchSpliterator<>(list, size), true);
    }

    private static final class BatchSpliterator<T> implements Spliterator<List<T>> {

        private final int splitSize;
        private List<T> list;

        private BatchSpliterator(final List<T> list, final int splitSize) {

            Objects.requireNonNull(list);

            if (splitSize <= 0) {
                throw new IllegalArgumentException("Invalid spitSize" + splitSize);
            }

            this.splitSize = splitSize;
            this.list = Collections.unmodifiableList(list);
        }

        @Override
        public boolean tryAdvance(Consumer<? super List<T>> action) {
            action.accept(list);
            return false;
        }

        @Override
        public Spliterator<List<T>> trySplit() {
            if (this.list.size() <= splitSize) {
                return null;
            }

            final List<T> subList = this.list.subList(0, splitSize);
            this.list = list.subList(splitSize, list.size());
            return new BatchSpliterator<>(subList, splitSize);
        }

        @Override
        public long estimateSize() {
            return (list.size() - 1) / splitSize + 1;
        }

        @Override
        public int characteristics() {
            return SIZED | SUBSIZED | IMMUTABLE;
        }
    }
}
