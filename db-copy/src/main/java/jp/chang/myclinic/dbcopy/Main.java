package jp.chang.myclinic.dbcopy;

import org.sqlite.SQLiteDataSource;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@CommandLine.Command(name = "db-copy", description = "Copies database data.")
public class Main implements Runnable {

    @CommandLine.Parameters(paramLabel = "source", index = "0", description = "Source database")
    private String dbSrc;
    @CommandLine.Parameters(paramLabel = "destination", index = "1", description = "Destination database")
    private String dbDst;
    @CommandLine.Option(names = {"--help"}, usageHelp = true, description = "Prints help.")
    private boolean help;

    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }

    @Override
    public void run() {
        try (Connection connSrc = getConnection(dbSrc);
             Connection connDst = getConnection(dbDst)) {
            System.out.println(connSrc);
            System.out.println(connDst);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection(String db) throws Exception {
        if (db.equals("mysql")) {
            String host = System.getenv("MYCLINIC_DB_HOST");
            int port = 3306;
            String database = "myclinic";
            boolean useSSL = false;
            String user = System.getenv("MYCLINIC_DB_USER");
            String pass = System.getenv("MYCLINIC_DB_PASS");
            String url = String.format("jdbc:mysql://%s:%d/%s?zeroDateTimeBehavior=convertToNull" +
                            "&noDatetimeStringSync=true&useUnicode=true&characterEncoding=utf8" +
                            "&useSSL=%s&serverTimezone=JST",
                    host, port, database, useSSL ? "true" : "false");
            return DriverManager.getConnection(url, user, pass);
        } else if (db.equals("sqlite")) {
            String dbFile = "work/copied.db";
            if( Files.exists(Paths.get(dbFile))){
                Files.delete(Paths.get(dbFile));
            }
            Class.forName("org.sqlite.JDBC");
            SQLiteDataSource ds = new SQLiteDataSource();
            String url = "jdbc:sqlite:" + dbFile;
            return DriverManager.getConnection(url, null, null);
        } else {
            System.err.printf("Unknown database: %s\n", db);
            System.err.println("Supported databases are: mysql, postgresql, and sqlite");
            System.exit(1);
            return null;
        }
    }

}

