package carsharing.repository;

import carsharing.models.Persistable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class Repository<T extends Persistable> implements AutoCloseable {
    private final String SELECT_BY_ID;
    private final String SELECT_ALL;
    private final String INSERT;
    private final Connection connection;
    protected PreparedStatement update;
    private PreparedStatement selectById;
    private PreparedStatement selectAll;
    private PreparedStatement insert;

    public Repository(Connection connection, String sqlSelectById, String sqlCreateTable, String sqlSelectAll,
                      String sqlInsert) throws SQLException {

        try (Statement createStmt = connection.createStatement()) {
            createStmt.execute(sqlCreateTable);
        }
        this.connection = connection;
        SELECT_BY_ID = sqlSelectById;
        SELECT_ALL = sqlSelectAll;
        INSERT = sqlInsert;
    }

    protected abstract T toObject(ResultSet rs) throws SQLException;

    protected abstract Object[] toParams(T t);

    public T selectById(int id) {
        try {
            if (selectById == null) selectById = prepareStatement(SELECT_BY_ID);
            applyParameters(selectById, id);
            ResultSet rs = selectById.executeQuery();
            if (rs.next()) return toObject(rs);
        } catch (SQLException e) {
            handleException(e);
        }
        return null;
    }

    public List<T> selectAll(Object... params) {
        try {
            if (selectAll == null) selectAll = prepareStatement(SELECT_ALL);
            applyParameters(selectAll, params);
            ResultSet rs = selectAll.executeQuery();
            var list = new ArrayList<T>();
            while (rs.next()) {
                list.add(toObject(rs));
            }
            return list;
        } catch (Exception e) {
            handleException(e);
        }

        return Collections.emptyList();
    }

    public void insert(T t) {
        try {
            if (insert == null) insert = prepareStatement(INSERT);
            applyParameters(insert, toParams(t));
            insert.executeUpdate();
        } catch (Exception e) {
            handleException(e);
        }
    }

    protected void applyParameters(PreparedStatement ps, Object... params) throws SQLException {
        ps.clearParameters();
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    protected void handleException(Exception e) {
        if (e instanceof SQLException) {
            handleSQLException((SQLException) e);
        }
        System.err.println(e.getMessage());
        e.printStackTrace();
    }

    private void handleSQLException(SQLException e) {
        System.out.printf("SQL State: %s, SQL Error Code: %d", e.getSQLState(), e.getErrorCode());
        e.iterator().forEachRemaining(t -> {
            System.out.printf("Cause: %s%nMessage: %s%n", t.getCause(), t.getLocalizedMessage());
            e.printStackTrace();
        });
    }

    protected PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    @Override
    public void close() throws SQLException {
        var exceptions = Stream.of(selectById, selectAll, insert, update)
                .filter(Objects::nonNull)
                .map(this::closePreparedStatement)
                .filter(Objects::nonNull)
                .reduce((e1, e2) -> {
                    e1.setNextException(e2);
                    return e1;
                });
        if (exceptions.isPresent()) {
            throw exceptions.get();
        }
    }

    protected SQLException closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                return e;
            }
        }
        return null;
    }
}
