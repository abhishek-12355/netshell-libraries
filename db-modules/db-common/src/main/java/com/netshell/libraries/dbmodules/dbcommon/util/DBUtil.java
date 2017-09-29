package com.netshell.libraries.dbmodules.dbcommon.util;

import com.netshell.libraries.utilities.common.Consumer;
import com.netshell.libraries.utilities.common.Function;
import com.netshell.libraries.utilities.common.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek
 * on 10/15/2016.
 */
public final class DBUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBUtil.class);

    private DBUtil() {
    }

    public static void executeQuery(
            final String query,
            final List<? extends Object> queryParameters,
            final Consumer<ResultSet, ? extends SQLException> consumer) {
        executeQuery(query, queryParameters, consumer, false);
    }

    public static void executeQuery(
            final String query,
            final List<? extends Object> queryParameters,
            final Consumer<ResultSet, ? extends SQLException> consumer,
            final boolean newConnection
    ) {
        DBUtil.execute(newConnection, connection -> {
            try (final PreparedStatement statement = connection.prepareStatement(query)) {
                setParameters(queryParameters, statement);
                try (final ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet != null) {
                        consumer.accept(resultSet);
                    } else {
                        LOGGER.debug("ResultSet is NULL. Skipping Consumer");
                    }
                }
            }
            return null;
        });
    }

    public static int executeUpdate(
            final String query,
            final List<? extends Object> queryParameters
    ) {

        return executeUpdate(query, queryParameters, false);
    }

    public static int executeUpdate(
            final String query,
            final List<? extends Object> queryParameters,
            final boolean newConnection
    ) {

        return DBUtil.execute(newConnection, connection -> {
            try (final PreparedStatement statement = connection.prepareStatement(query)) {
                setParameters(queryParameters, statement);
                return statement.executeUpdate();
            }
        });
    }

    public static void executeProcedure(
            final String query,
            final List<? extends Object> queryParameters,
            final List<SQLType> outParameters,
            final Consumer<List, ? extends SQLException> consumer
    ) {
        executeProcedure(query, queryParameters, outParameters, consumer, false);
    }


    public static void executeProcedure(
            final String query,
            final List<? extends Object> queryParameters,
            final List<SQLType> outParameters,
            final Consumer<List, ? extends SQLException> consumer,
            final boolean newConnection
    ) {
        DBUtil.execute(newConnection, connection -> {
            try (final CallableStatement statement = connection.prepareCall(query)) {
                setParameters(queryParameters, statement);
                setOutParameters(queryParameters.size(), outParameters, statement);
                statement.execute();
                consumer.accept(createResult(queryParameters.size(), outParameters, statement));
            }
            return null;
        });
    }

    private static <R> R execute(
            final boolean newConnection,
            final Function<Connection, R, ? extends SQLException> function
    ) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection(newConnection);
            return function.apply(connection);
        } catch (SQLException e) {
            LOGGER.error("SQLException Caught", e);
            throw new RuntimeException(e);
        } finally {
            if (newConnection && connection != null) {
                LOGGER.debug("Closing Connection as newConnection was specified");
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private static void setParameters(List<? extends Object> queryParameters, PreparedStatement statement) throws SQLException {
        for (int i = 0; i < queryParameters.size(); i++) {
            final Object o = queryParameters.get(i);


            if (o == null) {
                LOGGER.trace("Query Parameter {0} is null", i);
                statement.setNull(i + 1, Types.VARCHAR);
                continue;
            }

            LOGGER.trace("Query Parameter {0}={1}", i, o.toString());
            if (o instanceof Integer) {
                statement.setInt(i + 1, (Integer) o);
            } else if (o instanceof Boolean) {
                statement.setBoolean(i + 1, ((Boolean) o));
            } else if (o instanceof java.util.Date) {
                statement.setDate(i + 1, new Date(((java.util.Date) o).getTime()));
            } else if (o instanceof byte[]) {
                final Blob blob = statement.getConnection().createBlob();
                blob.setBytes(0, (byte[]) o);
                statement.setBlob(i + 1, blob);
            } else if (o instanceof InputStream) {
                statement.setBlob(i + 1, (InputStream) o);
            } else {
                statement.setString(i + 1, o.toString());
            }
        }
    }

    private static void setOutParameters(int start, List<SQLType> outParameters, CallableStatement statement) throws SQLException {
        for (int i = 0; i < outParameters.size(); i++) {
            final int index = start + i + 1;
            statement.registerOutParameter(index, outParameters.get(i));
        }
    }

    private static List createResult(int start, List<SQLType> outParameters, CallableStatement statement) throws SQLException {
        final List<Object> list = new ArrayList<>();
        for (int i = 0; i < outParameters.size(); i++) {
            final int index = start + i + 1;
            final SQLType o = outParameters.get(i);

            if (o.equals(JDBCType.DATE)) {
                list.add(statement.getDate(index));
            } else if (o.equals(JDBCType.BOOLEAN)) {
                list.add(statement.getBoolean(index));
            } else if (o.equals(JDBCType.INTEGER)) {
                list.add(statement.getInt(index));
            } else if (o.equals(JDBCType.BLOB)) {
                final Blob blob = statement.getBlob(index);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    IOUtils.inputToOutputStream(blob.getBinaryStream(), out);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    blob.free();
                }
                list.add(out.toByteArray());
            } else {
                list.add(statement.getString(index));
            }
        }
        return list;
    }

    public static void closeConnection(boolean commit) {
        ConnectionManager.closeConnection(commit);
    }

    public static final class Holder<T> {
        public T item;
    }

}