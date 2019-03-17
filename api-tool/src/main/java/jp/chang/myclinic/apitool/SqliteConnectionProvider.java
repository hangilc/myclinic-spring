package jp.chang.myclinic.apitool;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnectionProvider {

    public static Connection get(){
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:backend-sqlite/src/main/resources/schema-only.sqlite";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
