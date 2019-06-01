package jp.chang.myclinic.dbcopy;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.backendmysql.MysqlDataSourceProvider;
import jp.chang.myclinic.backendmysql.MysqlTableSet;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
import jp.chang.myclinic.backendsqlite.SqliteDataSource;
import picocli.CommandLine;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Function;

@CommandLine.Command(name = "db-copy", description = "Copies database data.")
public class Main implements Runnable {

    @CommandLine.Option(names = {"--help"}, usageHelp = true, description = "Prints help.")
    private boolean help;
    @CommandLine.Option(names = {"--statics-only"}, description = "Copies only masters and static data.")
    private boolean staticsOnly;
    @CommandLine.Parameters(paramLabel = "source", index = "0", description = "Source database")
    private String dbSrc;
    @CommandLine.Parameters(paramLabel = "destination", index = "1", description = "Destination database")
    private String dbDst;

    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }

    @Override
    public void run() {
        DataSource dsSrc = getDataSource(dbSrc);
        DataSource dsDst = getDataSource(dbDst);
        DbBackend dbBackendSrc = new DbBackend(new DBImpl(dsSrc), getTableSetCreator(dbSrc));
        DbBackend dbBackendDst = new DbBackend(new DBImpl(dsDst), getTableSetCreator(dbDst));
        TableSet tsSrc = dbBackendSrc.getTableSet();
        TableSet tsDst = dbBackendDst.getTableSet();

        // statics
        copyDatabase(dbBackendSrc, tsSrc.byoumeiMasterTable, dbBackendDst, tsDst.byoumeiMasterTable);
        copyDatabase(dbBackendSrc, tsSrc.iyakuhinMasterTable, dbBackendDst, tsDst.iyakuhinMasterTable);
        copyDatabase(dbBackendSrc, tsSrc.kizaiMasterTable, dbBackendDst, tsDst.kizaiMasterTable);
        copyDatabase(dbBackendSrc, tsSrc.shinryouMasterTable, dbBackendDst, tsDst.shinryouMasterTable);
        copyDatabase(dbBackendSrc, tsSrc.shuushokugoMasterTable, dbBackendDst, tsDst.shuushokugoMasterTable);

        // semi-dynamics
        copyDatabase(dbBackendSrc, tsSrc.pharmaDrugTable, dbBackendDst, tsDst.pharmaDrugTable);
        copyDatabase(dbBackendSrc, tsSrc.prescExampleTable, dbBackendDst, tsDst.prescExampleTable);

        if( !staticsOnly ) {
            // dynamics
            copyDatabase(dbBackendSrc, tsSrc.patientTable, dbBackendDst, tsDst.patientTable);
            copyDatabase(dbBackendSrc, tsSrc.shahokokuhoTable, dbBackendDst, tsDst.shahokokuhoTable);
            copyDatabase(dbBackendSrc, tsSrc.koukikoureiTable, dbBackendDst, tsDst.koukikoureiTable);
            copyDatabase(dbBackendSrc, tsSrc.roujinTable, dbBackendDst, tsDst.roujinTable);
            copyDatabase(dbBackendSrc, tsSrc.kouhiTable, dbBackendDst, tsDst.kouhiTable);
            copyDatabase(dbBackendSrc, tsSrc.visitTable, dbBackendDst, tsDst.visitTable);
            copyDatabase(dbBackendSrc, tsSrc.textTable, dbBackendDst, tsDst.textTable);
            copyDatabase(dbBackendSrc, tsSrc.drugTable, dbBackendDst, tsDst.drugTable);
            copyDatabase(dbBackendSrc, tsSrc.drugAttrTable, dbBackendDst, tsDst.drugAttrTable);
            copyDatabase(dbBackendSrc, tsSrc.shinryouTable, dbBackendDst, tsDst.shinryouTable);
            copyDatabase(dbBackendSrc, tsSrc.shinryouAttrTable, dbBackendDst, tsDst.shinryouAttrTable);
            copyDatabase(dbBackendSrc, tsSrc.shoukiTable, dbBackendDst, tsDst.shoukiTable);
            copyDatabase(dbBackendSrc, tsSrc.wqueueTable, dbBackendDst, tsDst.wqueueTable);
            copyDatabase(dbBackendSrc, tsSrc.chargeTable, dbBackendDst, tsDst.chargeTable);
            copyDatabase(dbBackendSrc, tsSrc.conductTable, dbBackendDst, tsDst.conductTable);
            copyDatabase(dbBackendSrc, tsSrc.conductDrugTable, dbBackendDst, tsDst.conductDrugTable);
            copyDatabase(dbBackendSrc, tsSrc.conductKizaiTable, dbBackendDst, tsDst.conductKizaiTable);
            copyDatabase(dbBackendSrc, tsSrc.conductShinryouTable, dbBackendDst, tsDst.conductShinryouTable);
            copyDatabase(dbBackendSrc, tsSrc.gazouLabelTable, dbBackendDst, tsDst.gazouLabelTable);
            copyDatabase(dbBackendSrc, tsSrc.diseaseTable, dbBackendDst, tsDst.diseaseTable);
            copyDatabase(dbBackendSrc, tsSrc.diseaseAdjTable, dbBackendDst, tsDst.diseaseAdjTable);
            copyDatabase(dbBackendSrc, tsSrc.paymentTable, dbBackendDst, tsDst.paymentTable);
            copyDatabase(dbBackendSrc, tsSrc.pharmaQueueTable, dbBackendDst, tsDst.pharmaQueueTable);
            copyDatabase(dbBackendSrc, tsSrc.practiceLogTable, dbBackendDst, tsDst.practiceLogTable);
            copyDatabase(dbBackendSrc, tsSrc.hotlineTable, dbBackendDst, tsDst.hotlineTable);
            copyDatabase(dbBackendSrc, tsSrc.intraclinicPostTable, dbBackendDst, tsDst.intraclinicPostTable);
            copyDatabase(dbBackendSrc, tsSrc.intraclinicCommentTable, dbBackendDst, tsDst.intraclinicCommentTable);
            copyDatabase(dbBackendSrc, tsSrc.intraclinicTagTable, dbBackendDst, tsDst.intraclinicTagTable);
            copyDatabase(dbBackendSrc, tsSrc.intraclinicTagPostTable, dbBackendDst, tsDst.intraclinicTagPostTable);
        }
    }

    private <DTO> void copyDatabase(DbBackend dbBackendSrc, TableBaseInterface<DTO> tableInterfaceSrc,
                                    DbBackend dbBackendDst, TableBaseInterface<DTO> tableInterfaceDst){
        dbBackendSrc.proc(backend -> {
            String sql = backend.xlate("select * from " + tableInterfaceSrc.getTableName(), tableInterfaceSrc);
            if( tableInterfaceSrc.getDtoName().equals("Payment") ){ // in order to workaround duplicate rows
                sql = backend.xlate("select * from " + tableInterfaceSrc.getTableName() +
                        " group by *", tableInterfaceSrc);
            }
            List<DTO> rows = backend.getQuery().query(sql, tableInterfaceSrc);
            System.out.println(sql);
            System.out.printf("%s %d items\n", tableInterfaceSrc.getTableName(), rows.size());
            dbBackendDst.txProc(backendDst -> {
                tableInterfaceDst.batchCopy(rows);
            });
        });
    }

    private DataSource getDataSource(String db) {
        switch (db) {
            case "mysql":
                return MysqlDataSourceProvider.create();
            case "sqlite":
                return SqliteDataSource.createFromDbFile("work/copied.db");
            default:
                throw new RuntimeException("Unknown database: " + db);
        }
    }

    private Function<Query, TableSet> getTableSetCreator(String db) {
        switch (db) {
            case "mysql":
                return MysqlTableSet::create;
            case "sqlite":
                return SqliteTableSet::create;
            default:
                throw new RuntimeException("Cannot find TableSet creator: " + db);
        }
    }

//    private Connection getConnection(String db) throws Exception {
//        if (db.equals("mysql")) {
//            String host = System.getenv("MYCLINIC_DB_HOST");
//            int port = 3306;
//            String database = "myclinic";
//            boolean useSSL = false;
//            String user = System.getenv("MYCLINIC_DB_USER");
//            String pass = System.getenv("MYCLINIC_DB_PASS");
//            String url = String.format("jdbc:mysql://%s:%d/%s?zeroDateTimeBehavior=convertToNull" +
//                            "&noDatetimeStringSync=true&useUnicode=true&characterEncoding=utf8" +
//                            "&useSSL=%s&serverTimezone=JST",
//                    host, port, database, useSSL ? "true" : "false");
//            return DriverManager.getConnection(url, user, pass);
//        } else if (db.equals("sqlite")) {
//            String dbFile = "work/copied.db";
//            if( Files.exists(Paths.get(dbFile))){
//                Files.delete(Paths.get(dbFile));
//            }
//            Class.forName("org.sqlite.JDBC");
//            SQLiteDataSource ds = new SQLiteDataSource();
//            String url = "jdbc:sqlite:" + dbFile;
//            return DriverManager.getConnection(url, null, null);
//        } else {
//            System.err.printf("Unknown database: %s\n", db);
//            System.err.println("Supported databases are: mysql, postgresql, and sqlite");
//            System.exit(1);
//            return null;
//        }
//    }

}

