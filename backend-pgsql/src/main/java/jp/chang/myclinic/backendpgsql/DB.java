package jp.chang.myclinic.backendpgsql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {

    private static HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        String host = System.getenv("MYCLINIC_POSTGRES_HOST");
        String user = System.getenv("MYCLINIC_POSTGRES_USER");
        String pass = System.getenv("MYCLINIC_POSTGRES_PASS");
        config.setJdbcUrl("jdbc:postgresql://" + host + "/myclinic");
        config.setUsername(user);
        config.setPassword(pass);
        DB.ds = new HikariDataSource(config);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("closing data souce");
            DB.ds.close();
        }));
    }

    private DB() {

    }

    private static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static <T>  T get(String sql, Query.SqlConsumer<PreparedStatement> statementSetter,
                             Query.SqlMapper<T> mapper) {
        try (Connection conn = getConnection()){
            return Query.get(conn, sql, statementSetter, mapper);
        } catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public interface TransactionProc {
        void execute(Connection conn) throws SQLException;
    }

    public void tx(TransactionProc proc){
        Connection conn = null;
        try {
            conn = getConnection();
            proc.execute(conn);
        } catch(Exception ex){
            ex.printStackTrace();
            if( conn != null ){
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if( conn != null ){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
