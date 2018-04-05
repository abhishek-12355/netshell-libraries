package com.netshell.libraries.dbmodules.dbenum;

import com.netshell.libraries.dbmodules.dbenum.initializers.Initializable;

import java.util.Collection;
import java.util.Optional;

/**
 * @author ashekha
 * @since 11/1/2016.
 */
public interface IDBEnum<I, O, T extends Initializable<O>> {
    /**
     * Returns an {@link Optional} object which wraps the enum object based on the Id.
     *
     * @param id of the enumeration object
     * @return DBEnum object based on the Id.
     */
    Optional<T> findById(final O id);

    /**
     * Returns an enum object based on the Id. if no enum object is found throws {@link DBEnumException}.
     *
     * @param id of the enumeration object
     * @return DBEnum object based on the Id.
     */
    T getById(final O id);

    /**
     * filter all the enumerations based on filterCriteria
     *
     * @param filterCriteria specifies the specific attributes based on which a filtered Collection is returned.
     * @return {@link Collection} of filtered objects
     */
    Collection<T> filter(final T filterCriteria);

    Optional<T> findFirst(final T filterCriteria);

    Collection<T> getEnumCollection();

    void refresh();
}
