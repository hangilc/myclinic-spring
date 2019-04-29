package jp.chang.myclinic.backendsqlite;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqliteDataSource {

    public static DataSource createFromDbFile(String dbFile) {
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteDataSource ds = new SQLiteDataSource();
            String url = "jdbc:sqlite:" + dbFile;
            ds.setUrl(url);
            return ds;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource createTemporaryFromDbFile(String dbFile){
        try {
            Class.forName("org.sqlite.JDBC");
            Path temp = Files.createTempFile("sqlite", ".db");
            temp.toFile().deleteOnExit();
            Files.copy(Paths.get(dbFile), temp, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(temp);
            SQLiteConfig config = new SQLiteConfig();
            config.setBusyTimeout(60000);
            SQLiteDataSource ds = new SQLiteDataSource(config);
            String url = "jdbc:sqlite:" + temp.toString();
            ds.setUrl(url);
            return ds;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
