package com.netshell.libraries.dbmodules.dbenum;

import com.netshell.libraries.dbmodules.dbenum.datasource.IterableDataSource;
import com.netshell.libraries.dbmodules.dbenum.initializers.Initializable;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Abhishek
 * on 3/26/2016.
 */
public class DBEnumTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBEnumTest.class);
    private static final List<String> STRINGS = Arrays.asList("Abhishek", "Shashank", "Mayank", "Abhishek", "Kaish", "Ankit", "Abhishek");

    @BeforeAll
    public static void before() {
        DBEnum.initialize(TestEntity.class, TestEntity::new, new IterableDataSource<>(STRINGS));
        DBEnum.initializeDefault(new IterableDataSource<DBEnum.DBDefaultEnumEntity<Integer>>(Collections.singletonList(
                new DBEnum.DBDefaultEnumEntity<Integer>() {
                    @Override
                    public String getId() {
                        return TestEntity.class.getName();
                    }

                    @Override
                    public Integer getValue() {
                        return getDefault().getId();
                    }
                }
        )));
    }

    private static TestEntity getDefault() {
        return new TestEntity(0, "Abhishek");
    }

    @Test
    public void testFindById() throws Exception {
        Assert.assertEquals("Abhishek", DBEnum.findById(TestEntity.class, 0).get().getName());
        Assert.assertEquals("Shashank", DBEnum.findById(TestEntity.class, 1).get().getName());

        DBEnum.findById(TestEntity.class, 7);

    }

    @Test
    public void testGetById() throws Exception {
        Assert.assertEquals("Abhishek", DBEnum.getById(TestEntity.class, 0).getName());
        Assert.assertEquals("Shashank", DBEnum.getById(TestEntity.class, 1).getName());
    }

    @Test
    public void testFilter() throws Exception {
        TestEntity testEntity = new TestEntity("Abhishek");
        LOGGER.info(Arrays.deepToString(DBEnum.filter(TestEntity.class, testEntity).toArray()));
    }

    @Test
    public void testCollection() {
        TestEntity.count = 0;
        final List<TestEntity> enumCollection = new ArrayList<>(DBEnum.getEnumCollection(TestEntity.class));
        final List<TestEntity> collect = STRINGS.stream().map(TestEntity::new).collect(Collectors.toList());
//        Collections.sort(enumCollection, (o1, o2) -> o1.getId().compareTo(o2.getId()));
//        Collections.sort(collect, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        Assert.assertEquals(collect, enumCollection);
    }

    @Test
    public void testDefault() {
        Assert.assertEquals(DBEnum.getDefault(TestEntity.class), getDefault());
    }

    private static final class TestEntity implements Initializable<Integer> {

        static int count = 0;

        private String name;
        private int id;

        public TestEntity(int i, String identity) {
            this.id = i;
            this.name = identity;
        }

        public TestEntity(String identity) {
            this.id = count++;
            this.name = identity;
        }

        public String getName() {
            return name;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return "TestEntity{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestEntity that = (TestEntity) o;

            return id == that.id && !(name != null ? !name.equals(that.name) : that.name != null);

        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + id;
            return result;
        }
    }
}