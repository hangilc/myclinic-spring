package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.TableXferer;
import jp.chang.myclinic.dbxfer.mysql.ConverterMapMySql;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    private static void usage(){
        System.err.println("Usage: dbxfer source dest");
        System.err.println("  source and dest can be 'mysql' or 'postgresql'");
        System.err.println("  database name can be specified following color");
        System.err.println("  default database name is 'myclinic'");
        System.err.println("  for example, mysql:myclinic_test");
    }

    private static String[] parseDatabaseSpec(String spec){
        int index = spec.indexOf(':');
        if( index < 0 ){
            return new String[]{ spec, "myclinic" };
        } else {
            return new String[]{ spec.substring(0, index), spec.substring(index+1) };
        }
    }

    private static Database createDatabase(String spec) throws Exception {
        String[] parts = parseDatabaseSpec(spec);
        if( "mysql".equals(parts[0]) ){
            Class.forName("org.postgresql.Driver");
            Connection mysqlConn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/" + parts[1] + "?useSSL=false&zeroDateTimeBehavior=convertToNull",
                    System.getenv("MYCLINIC_DB_USER"),
                    System.getenv("MYCLINIC_DB_PASS"));
            return new Database(mysqlConn, new MySqlDb());
        } else if( "postgresql".equals(parts[0]) ){
            Connection psqlConn = DriverManager.getConnection(
                    String.format("jdbc:postgresql://%s:%d/%s", "localhost", 5432, parts[1]),
                    System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
            return new Database(psqlConn, new PostgreSqlDb());
        } else {
            throw new RuntimeException("Invalid source/dest spec: " + spec);
        }
    }

    public static void main( String[] args ) throws Exception {
        Database srcDatabase = null;
        Database dstDatabase = null;
        if( args.length == 2 ){
            srcDatabase = createDatabase(args[0]);
            dstDatabase = createDatabase(args[1]);
        } else {
            usage();
            System.exit(1);
        }
        new Main(srcDatabase, dstDatabase).xferAll();
        srcDatabase.getConn().close();
        dstDatabase.getConn().close();
    }

    private Database src;
    private Database dst;
    private TableXferer tableXferer;

    Main(Database src, Database dst) throws Exception {
        this.src = src;
        this.dst = dst;
        this.tableXferer = new TableXferer(src.getConn(), dst.getConn(), new ConverterMapMySql());
    }

    void xferAll() {
        MyclinicDb srcDb = src.getDb();
        MyclinicDb dstDb = dst.getDb();
        //tableXferer.xfer(srcDb.getIyakuhinMaster(), dstDb.getIyakuhinMaster());
        //tableXferer.xfer(srcDb.getShinryouMaster(), dstDb.getShinryouMaster());
        tableXferer.xfer(srcDb.getKizaiMaster(), dstDb.getKizaiMaster());
    }

}

