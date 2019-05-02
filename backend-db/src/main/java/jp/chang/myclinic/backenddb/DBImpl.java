package jp.chang.myclinic.backenddb;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class DBImpl implements DB {

    private DataSource ds;
    private ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    public DBImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Supplier<Connection> getConnectionProvider() {
        return () -> threadLocalConnection.get();
    }

    private Connection openConnection() throws SQLException {
        Connection conn = ds.getConnection();
        threadLocalConnection.set(conn);
        return conn;
    }

    @Override
    public <T> T query(Proc<T> proc) {
        try (Connection conn = openConnection()) {
            conn.setAutoCommit(true);
            return proc.call();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            threadLocalConnection.set(null);
        }
    }

    @Override
    public <T> T tx(Proc<T> proc) {
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            T value = proc.call();
            conn.commit();
            return value;
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            throw new RuntimeException(ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            threadLocalConnection.set(null);
        }
    }

}
