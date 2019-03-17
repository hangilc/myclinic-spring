package jp.chang.myclinic.backendpgsql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DB {

    private DB() {

    }

    private static DataSource ds;
    private static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    static {
        Query.setConnectionProvider(threadLocalConnection::get);
    }

    private static Connection openConnection() throws SQLException {
        Connection conn = ds.getConnection();
        threadLocalConnection.set(conn);
        return conn;
    }

    public static void setDataSource(DataSource ds){
        DB.ds = ds;
    }

    private static Connection getConnection() {
        return threadLocalConnection.get();
    }

    public interface ProcVoid {
        void exec() throws SQLException;
    }

    public interface Proc<T> {
        T call() throws SQLException;
    }

    public static void proc(ProcVoid proc){
        try (Connection conn = openConnection()){
            conn.setAutoCommit(true);
            proc.exec();
        } catch(SQLException e){
            throw new RuntimeException(e);
        } finally {
            threadLocalConnection.set(null);
        }
    }

    public static <T> T query(Proc<T> proc){
        try (Connection conn = openConnection()){
            conn.setAutoCommit(true);
            return proc.call();
        } catch(SQLException e){
            throw new RuntimeException(e);
        } finally {
            threadLocalConnection.set(null);
        }
    }

    public static <T> T tx(Proc<T> proc){
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            T value = proc.call();
            conn.commit();
            return value;
        } catch(Exception ex){
            if( conn != null ){
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            throw new RuntimeException(ex);
        } finally {
            if( conn != null ){
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
