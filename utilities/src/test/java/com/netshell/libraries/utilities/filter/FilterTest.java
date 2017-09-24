package com.netshell.libraries.utilities.filter;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Abhishek
 * Created on 12/15/2015.
 */
public class FilterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterTest.class);

    @Test
    public void filterPredicateTest() {
        Collection<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collection<Integer> oddIntegers = Arrays.asList(1, 3, 5, 7, 9);
        final Collection<Integer> filter = Filter.filter(integers, (Predicate<Integer>) integer -> integer % 2 != 0);
        LOGGER.info(Arrays.toString(filter.toArray()));
        assert filter.containsAll(oddIntegers);
    }

    @Test
    public void filterTest() {
        Collection<User> userCollection1 = Arrays.asList(getUserCollection1());
        Collection<User> userCollection2 = Arrays.asList(getUserCollection2());
        final Collection<User> filter = Filter.filter(userCollection1, new User("Abhishek"));
        LOGGER.info(Arrays.toString(filter.toArray()));
        assert filter.containsAll(userCollection2);
    }

    @Test
    public void filterIdTest() {
        Collection<User> userCollection1 = Arrays.asList(getUserCollection1());
        final List<User> filter = Filter.filter(userCollection1, new User(null, 1));
        LOGGER.info(Arrays.toString(filter.toArray()));
        assert filter.size() == 1 && filter.get(0).getId() == 1 && filter.get(0).getName().equals("Abhishek");
    }

    private User[] getUserCollection2() {
        return new User[]{
                new User("Abhishek", 1),
                new User("Abhishek", 3),
                new User("Abhishek", 5),
                new User("Abhishek", 7),
                new User("Abhishek", 9)
        };
    }

    private User[] getUserCollection1() {
        return new User[]{
                new User("Abhishek", 1),
                new User("Abhishek", 3),
                new User("Abhishek", 5),
                new User("Abhishek", 7),
                new User("Abhishek", 9),
                new User("Shashank", 2),
                new User("Shashank", 4),
                new User("Shashank", 6),
                new User("Shashank", 8),
                new User("Shashank", 10),
        };
    }

    private static class User {
        private String name;
        private int id;

        public User() {

        }

        public User(String name) {
            this.name = name;
        }

        public User(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.format("[%d %s]", id, name);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof User && id == ((User) obj).id && Objects.equals(name, ((User) obj).name);
        }
    }
}