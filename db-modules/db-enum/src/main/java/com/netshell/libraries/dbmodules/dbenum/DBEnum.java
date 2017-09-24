package com.netshell.libraries.dbmodules.dbenum;

import com.netshell.libraries.dbmodules.dbenum.datasource.DataSource;
import com.netshell.libraries.dbmodules.dbenum.initializers.Initializable;
import com.netshell.libraries.dbmodules.dbenum.initializers.Initializer;
import com.netshell.libraries.utilities.common.CommonUtils;
import com.netshell.libraries.utilities.common.ReflectionUtils;
import com.netshell.libraries.utilities.filter.Filter;

import java.util.*;

/**
 * @author Abhishek
 * Created on 12/13/2015.
 * <p>
 * DBEnum is a wrapper of Enum values as stored in a Database or some other datasource.
 */
public final class DBEnum<I, O, T extends Initializable<O>> {

    /**
     * EMPTY_ENUM follows a NULL Object pattern.
     * Wherever, in context of DBEnum, null is required to be returned EMPTY_ENUM is returned as a filler.
     */
    @SuppressWarnings("unchecked")
    private static final DBEnum EMPTY_ENUM = new DBEnum<>(
            ReflectionUtils.createIdentityProxy(Initializer.class),
            ReflectionUtils.createIdentityProxy(DataSource.class)
    );

    /**
     * delegate to manage DBEnum. Separation of Concern.
     */
    private final IDBEnum<I, O, T> delegate;

    /**
     * Constructor to create a DBEnum.
     *
     * @param initializer used to initialize the enumeration. This tells how the enumeration values
     *                    will be converted to Java objects.
     * @param dataSource  used to retrieve values into the enumeration. The actual values that are stored as Enumeration.
     */
    private DBEnum(final Initializer<T, I> initializer, final DataSource<I> dataSource) {
        this.delegate = new DBEnumInternal<>(initializer, dataSource);
    }

    /**
     * @param tEnumClass
     * @param initializer
     * @param dataSource
     * @param <TEntity>
     * @param <TEnumId>
     * @param <TEnum>
     */
    public static <TEntity, TEnumId, TEnum extends Initializable<TEnumId>>
    void initialize(
            final Class<TEnum> tEnumClass,
            final Initializer<TEnum, TEntity> initializer,
            final DataSource<TEntity> dataSource) {
        CommonUtils.getManager().registerSingleton(tEnumClass.getName(), new DBEnum<>(initializer, dataSource));
    }

    public static <TEntity extends DBDefaultEnumEntity<TEnumId>, TEnumId>
    void initializeDefault(final DataSource<TEntity> dataSource) {
        initialize(DBDefaultEnum.class, DBDefaultEnum::new, dataSource);
    }

    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    Optional<TEnum> findById(final Class<TEnum> tEnumClass, final TEnumId id) {
        return DBEnum.getDBEnumObject(tEnumClass).findById(id);
    }

    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    Optional<TEnum> findByIdOrDefault(final Class<TEnum> tEnumClass, final TEnumId id) {
        final Optional<TEnum> byId = DBEnum.getDBEnumObject(tEnumClass).findById(id);
        return byId.isPresent() ? byId : DBEnum.findDefault(tEnumClass);
    }

    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    TEnum getById(final Class<TEnum> tEnumClass, final TEnumId id) {
        return DBEnum.getDBEnumObject(tEnumClass).getById(id);
    }

    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    TEnum getByIdOrDefault(final Class<TEnum> tEnumClass, final TEnumId id) {
        return DBEnum.findByIdOrDefault(tEnumClass, id)
                .orElseThrow(() -> new DBEnumException("Unable to find: " + id.toString()));
    }

    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    Collection<TEnum> filter(final Class<TEnum> tEnumClass, final TEnum tEnum) {
        return DBEnum.getDBEnumObject(tEnumClass).filter(tEnum);
    }

    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    Optional<TEnum> findFirst(final Class<TEnum> tEnumClass, final TEnum tEnum) {
        return DBEnum.getDBEnumObject(tEnumClass).findFirst(tEnum);
    }

    @SuppressWarnings("unchecked")
    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    Optional<TEnum> findDefault(final Class<TEnum> tEnumClass) {
        final Optional<DBDefaultEnum> dbDefaultEnum =
                DBEnum.getDBEnumObject(DBDefaultEnum.class).findById(tEnumClass.getName());

        return dbDefaultEnum.isPresent()
                ? DBEnum.findById(tEnumClass, (TEnumId) dbDefaultEnum.get().getValue())
                : Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    TEnum getDefault(final Class<TEnum> tEnumClass) {
        final Optional<DBDefaultEnum> dbDefaultEnum =
                DBEnum.getDBEnumObject(DBDefaultEnum.class).findById(tEnumClass.getName());

        return DBEnum.getById(tEnumClass, (TEnumId) dbDefaultEnum.orElseThrow(
                () -> new DBEnumException("No Default Found: " + tEnumClass.getName())
        ).getValue());
    }

    @SuppressWarnings("unchecked")
    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    TEnum getDefaultOrNull(final Class<TEnum> tEnumClass) {
        final Optional<DBDefaultEnum> dbDefaultEnum =
                DBEnum.getDBEnumObject(DBDefaultEnum.class).findById(tEnumClass.getName());

        return dbDefaultEnum.isPresent()
                ? DBEnum.findById(tEnumClass, (TEnumId) dbDefaultEnum.get().getValue()).orElse(null)
                : null;
    }

    public static <TEnumId, TEnum extends Initializable<TEnumId>>
    Collection<TEnum> getEnumCollection(final Class<TEnum> tEnumClass) {
        return DBEnum.getDBEnumObject(tEnumClass).getEnumCollection();
    }

    @SuppressWarnings("unchecked")
    private static <TEntity, TEnumId, TEnum extends Initializable<TEnumId>>
    DBEnum<TEntity, TEnumId, TEnum> getDBEnumObject(final Class<TEnum> tEnumClass) {
        return CommonUtils.getManager().getSingleton(tEnumClass.getName(), DBEnum.class).orElseGet(DBEnum::empty);
    }

    @SuppressWarnings("unchecked")
    private static <TEntity, TEnumId, TEnum extends Initializable<TEnumId>>
    DBEnum<TEntity, TEnumId, TEnum> empty() {
        return (DBEnum<TEntity, TEnumId, TEnum>) EMPTY_ENUM;
    }

    /**
     * Returns an {@link Optional<T>} object which wraps the enum object based on the Id.
     *
     * @param id of the enumeration object
     * @return DBEnum object based on the Id.
     */
    private Optional<T> findById(final O id) {
        return delegate.findById(id);
    }

    /**
     * Returns an enum object based on the Id. if no enum object is found throws {@link DBEnumException}.
     *
     * @param id of the enumeration object
     * @return DBEnum object based on the Id.
     */
    private T getById(final O id) {
        return delegate.getById(id);
    }

    /**
     * filter all the enumerations based on filterCriteria
     *
     * @param filterCriteria specifies the specific attributes based on which a filtered Collection is returned.
     * @return {@link Collection<T>} of filtered objects
     */
    private Collection<T> filter(final T filterCriteria) {
        return delegate.filter(filterCriteria);
    }

    private Optional<T> findFirst(final T filterCriteria) {
        return delegate.findFirst(filterCriteria);
    }

    private Collection<T> getEnumCollection() {
        return delegate.getEnumCollection();
    }


    public interface DBDefaultEnumEntity<TEnumId> {
        String getId();

        TEnumId getValue();
    }

    private static final class DBDefaultEnum<TEnumId> implements Initializable<String> {

        private final String id;
        private final TEnumId value;

        DBDefaultEnum(final int index, final DBDefaultEnumEntity<TEnumId> entity) {
            this.id = entity.getId();
            this.value = entity.getValue();
        }

        TEnumId getValue() {
            return value;
        }

        @Override
        public String getId() {
            return id;
        }
    }

    private static final class DBEnumInternal<I, O, T extends Initializable<O>> implements IDBEnum<I, O, T> {
        /**
         * tMap is map containing all the enumeration values as retrived from the datasource
         */
        private final Map<O, T> tMap = new HashMap<>();

        /**
         * Constructor to create a DBEnum.
         *
         * @param initializer used to initialize the enumeration. This tells how the enumeration values
         *                    will be converted to Java objects.
         * @param dataSource  used to retrieve values into the enumeration. The actual values that are stored as Enumeration.
         */
        private DBEnumInternal(final Initializer<T, I> initializer, final DataSource<I> dataSource) {
            this.initialize(initializer, dataSource);
        }

        /**
         * Initialize the DBEnum object using the initializer and dataSource.
         *
         * @param initializer used to initialize the enumeration. This tells how the enumeration values
         *                    will be converted to Java objects.
         * @param dataSource  used to retrieve values into the enumeration. The actual values that are stored as Enumeration.
         */
        private void initialize(final Initializer<T, I> initializer, final DataSource<I> dataSource) {
            dataSource.activate();

            try (final DataSource<I> d = dataSource) {
                int i = 0;
                tMap.clear();
                while (d.hasNext()) {
                    final T item = initializer.initialize(i++, dataSource.next());
                    tMap.put(item.getId(), item);
                }
            } catch (Exception e) {
                throw new DBEnumException("Exception occurred", e);
            }
        }

        /**
         * Returns an {@link Optional<T>} object which wraps the enum object based on the Id.
         *
         * @param id of the enumeration object
         * @return DBEnum object based on the Id.
         */
        @Override
        public Optional<T> findById(final O id) {
            return Optional.ofNullable(tMap.get(id));
        }

        /**
         * Returns an enum object based on the Id. if no enum object is found throws {@link DBEnumException}.
         *
         * @param id of the enumeration object
         * @return DBEnum object based on the Id.
         */
        @Override
        public T getById(final O id) {
            if (!tMap.containsKey(id)) {
                throw new DBEnumException("Unable to find " + id.toString());
            }

            return tMap.get(id);
        }

        /**
         * filter all the enumerations based on filterCriteria
         *
         * @param filterCriteria specifies the specific attributes based on which a filtered Collection is returned.
         * @return {@link Collection<T>} of filtered objects
         */
        @Override
        public Collection<T> filter(final T filterCriteria) {
            return Filter.filter(this.tMap.values(), filterCriteria);
        }

        @Override
        public Optional<T> findFirst(final T filterCriteria) {
            return Filter.findFirst(this.tMap.values(), filterCriteria);
        }

        @Override
        public Collection<T> getEnumCollection() {
            return Collections.unmodifiableCollection(this.tMap.values());
        }
    }
}
