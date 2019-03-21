package jp.chang.myclinic.backendsqlite;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;

class SqliteDatabase {

    static DataSource createFromDbFile(String dbFile) {
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

    static DataSource createTemporaryFromDbFile(String dbFile){
        try {
            Class.forName("org.sqlite.JDBC");
            Path temp = Files.createTempFile("sqlite", ".db");
            temp.toFile().deleteOnExit();
            Files.copy(Paths.get(dbFile), temp, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(temp);
            SQLiteDataSource ds = new SQLiteDataSource();
            String url = "jdbc:sqlite:" + temp.toString();
            ds.setUrl(url);
            return ds;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
