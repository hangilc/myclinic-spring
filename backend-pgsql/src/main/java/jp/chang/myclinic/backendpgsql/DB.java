package jp.chang.myclinic.backendpgsql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
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

    public interface Proc<T> {
        T call(Connection conn) throws SQLException;
    }

    public static <T> T get(Proc<T> proc){
        try (Connection conn = getConnection()){
            return proc.call(conn);
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public <T> T tx(Proc<T> proc){
        Connection conn = null;
        try {
            conn = getConnection();
            T value = proc.call(conn);
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
        }
    }

}
