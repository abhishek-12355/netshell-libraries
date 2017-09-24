package com.netshell.libraries.dbmodules.dbenum.datasource;

import com.netshell.libraries.dbmodules.dbenum.DBEnum;
import com.netshell.libraries.dbmodules.dbenum.DBEnumException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Abhishek
 * Created on 12/26/2015.
 */

public abstract class SqlDataSourceBase<I, T extends SqlDataSourceBase.SqlRow<I>> implements DataSource<T> {

    private final ResultSet resultSet;
    private T tItem;

    public SqlDataSourceBase(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public void close() throws SQLException {
        resultSet.close();
    }

    @Override
    public boolean hasNext() {
        try {
            if (resultSet.next()) {
                tItem = mapRow(resultSet);
                return true;
            }
        } catch (SQLException e) {
            throw new DBEnumException("Error Occurred", e);
        }

        return false;
    }

    protected abstract T mapRow(ResultSet resultSet) throws SQLException;

    @Override
    public T next() {
        return tItem;
    }

    public interface SqlRow<T> {
        T getItem() throws SQLException;
    }

    public static final class DefaultSqlRow<T> implements SqlRow<DBEnum.DBDefaultEnumEntity<T>>, DBEnum.DBDefaultEnumEntity<T> {

        private final T value;
        private final String id;

        public DefaultSqlRow(final String id, final T value) {
            this.value = value;
            this.id = id;
        }

        @Override
        public DBEnum.DBDefaultEnumEntity<T> getItem() throws SQLException {
            return this;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public T getValue() {
            return this.value;
        }
    }
}
