package jp.chang.myclinic.apitool;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.function.Supplier;

public class SqliteConnectionProvider implements Supplier<Connection> {

    @Override
    public Connection get(){
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite::memory:";
            String schemaSource = Files.readString(Paths.get("backend-sqlite/src/main/sql/sqlite-schema.sql"));
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            for(String sql: schemaSource.split(";")){
                sql = sql.trim();
                if( sql.isEmpty() ){
                    continue;
                }
                stmt.execute(sql);
            }
            stmt.close();
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
