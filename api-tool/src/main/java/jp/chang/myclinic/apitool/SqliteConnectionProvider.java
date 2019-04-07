package jp.chang.myclinic.apitool;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.function.Supplier;

public class SqliteConnectionProvider implements Supplier<Connection> {

    private String dbFile;

    public SqliteConnectionProvider() {
        this("backend-sqlite/src/main/resources/schema-only.db");
    }

    public SqliteConnectionProvider(String dbFile) {
        System.out.println("dbFile: " + dbFile);
        this.dbFile = dbFile;
    }

    @Override
    public Connection get(){
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite::memory:";
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute("restore from '" + dbFile + "'");
            stmt.close();
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
