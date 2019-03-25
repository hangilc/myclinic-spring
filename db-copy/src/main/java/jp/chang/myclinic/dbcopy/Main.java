package jp.chang.myclinic.dbcopy;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.backenddb.TableSet;
import jp.chang.myclinic.backenddb.tableinterface.*;
import jp.chang.myclinic.backendmysql.MysqlDataSource;
import jp.chang.myclinic.backendmysql.MysqlTableSet;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
import jp.chang.myclinic.backendsqlite.SqliteDataSource;
import org.sqlite.SQLiteDataSource;
import picocli.CommandLine;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Function;

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
        DataSource dsSrc = getDataSource(dbSrc);
        DataSource dsDst = getDataSource(dbDst);
        DbBackend dbBackendSrc = new DbBackend(dsSrc, getTableSetCreator(dbSrc));
        DbBackend dbBackendDst = new DbBackend(dsDst, getTableSetCreator(dbDst));
        TableSet tsSrc = dbBackendSrc.getTableSet();
        TableSet tsDst = dbBackendDst.getTableSet();
        ByoumeiMasterTableInterface[] byoumeiMasterTables = new ByoumeiMasterTableInterface[]{tsSrc.byoumeiMasterTable, tsDst.byoumeiMasterTable};
        ChargeTableInterface[] chargeTables = new ChargeTableInterface[]{tsSrc.chargeTable, tsDst.chargeTable};
        ConductTableInterface[] conductTables = new ConductTableInterface[]{tsSrc.conductTable, tsDst.conductTable};
        ConductDrugTableInterface[] conductDrugTables = new ConductDrugTableInterface[]{tsSrc.conductDrugTable, tsDst.conductDrugTable};
        ConductKizaiTableInterface[] conductKizaiTables = new ConductKizaiTableInterface[]{tsSrc.conductKizaiTable, tsDst.conductKizaiTable};
        ConductShinryouTableInterface[] conductShinryouTables = new ConductShinryouTableInterface[]{tsSrc.conductShinryouTable, tsDst.conductShinryouTable};
        DiseaseTableInterface[] diseaseTables = new DiseaseTableInterface[]{tsSrc.diseaseTable, tsDst.diseaseTable};
        DiseaseAdjTableInterface[] diseaseAdjTables = new DiseaseAdjTableInterface[]{tsSrc.diseaseAdjTable, tsDst.diseaseAdjTable};
        DrugTableInterface[] drugTables = new DrugTableInterface[]{tsSrc.drugTable, tsDst.drugTable};
        DrugAttrTableInterface[] drugAttrTables = new DrugAttrTableInterface[]{tsSrc.drugAttrTable, tsDst.drugAttrTable};
        GazouLabelTableInterface[] gazouLabelTables = new GazouLabelTableInterface[]{tsSrc.gazouLabelTable, tsDst.gazouLabelTable};
        HotlineTableInterface[] hotlineTables = new HotlineTableInterface[]{tsSrc.hotlineTable, tsDst.hotlineTable};
        IntraclinicCommentTableInterface[] intraclinicCommentTables = new IntraclinicCommentTableInterface[]{tsSrc.intraclinicCommentTable, tsDst.intraclinicCommentTable};
        IntraclinicPostTableInterface[] intraclinicPostTables = new IntraclinicPostTableInterface[]{tsSrc.intraclinicPostTable, tsDst.intraclinicPostTable};
        IntraclinicTagTableInterface[] intraclinicTagTables = new IntraclinicTagTableInterface[]{tsSrc.intraclinicTagTable, tsDst.intraclinicTagTable};
        IntraclinicTagPostTableInterface[] intraclinicTagPostTables = new IntraclinicTagPostTableInterface[]{tsSrc.intraclinicTagPostTable, tsDst.intraclinicTagPostTable};
        IyakuhinMasterTableInterface[] iyakuhinMasterTables = new IyakuhinMasterTableInterface[]{tsSrc.iyakuhinMasterTable, tsDst.iyakuhinMasterTable};
        KizaiMasterTableInterface[] kizaiMasterTables = new KizaiMasterTableInterface[]{tsSrc.kizaiMasterTable, tsDst.kizaiMasterTable};
        KouhiTableInterface[] kouhiTables = new KouhiTableInterface[]{tsSrc.kouhiTable, tsDst.kouhiTable};
        KoukikoureiTableInterface[] koukikoureiTables = new KoukikoureiTableInterface[]{tsSrc.koukikoureiTable, tsDst.koukikoureiTable};
        PatientTableInterface[] patientTables = new PatientTableInterface[]{tsSrc.patientTable, tsDst.patientTable};
        PaymentTableInterface[] paymentTables = new PaymentTableInterface[]{tsSrc.paymentTable, tsDst.paymentTable};
        PharmaDrugTableInterface[] pharmaDrugTables = new PharmaDrugTableInterface[]{tsSrc.pharmaDrugTable, tsDst.pharmaDrugTable};
        PharmaQueueTableInterface[] pharmaQueueTables = new PharmaQueueTableInterface[]{tsSrc.pharmaQueueTable, tsDst.pharmaQueueTable};
        PracticeLogTableInterface[] practiceLogTables = new PracticeLogTableInterface[]{tsSrc.practiceLogTable, tsDst.practiceLogTable};
        PrescExampleTableInterface[] prescExampleTables = new PrescExampleTableInterface[]{tsSrc.prescExampleTable, tsDst.prescExampleTable};
        RoujinTableInterface[] roujinTables = new RoujinTableInterface[]{tsSrc.roujinTable, tsDst.roujinTable};
        ShahokokuhoTableInterface[] shahokokuhoTables = new ShahokokuhoTableInterface[]{tsSrc.shahokokuhoTable, tsDst.shahokokuhoTable};
        ShinryouTableInterface[] shinryouTables = new ShinryouTableInterface[]{tsSrc.shinryouTable, tsDst.shinryouTable};
        ShinryouAttrTableInterface[] shinryouAttrTables = new ShinryouAttrTableInterface[]{tsSrc.shinryouAttrTable, tsDst.shinryouAttrTable};
        ShinryouMasterTableInterface[] shinryouMasterTables = new ShinryouMasterTableInterface[]{tsSrc.shinryouMasterTable, tsDst.shinryouMasterTable};
        ShoukiTableInterface[] shoukiTables = new ShoukiTableInterface[]{tsSrc.shoukiTable, tsDst.shoukiTable};
        ShuushokugoMasterTableInterface[] shuushokugoMasterTables = new ShuushokugoMasterTableInterface[]{tsSrc.shuushokugoMasterTable, tsDst.shuushokugoMasterTable};
        TextTableInterface[] textTables = new TextTableInterface[]{tsSrc.textTable, tsDst.textTable};
        VisitTableInterface[] visitTables = new VisitTableInterface[]{tsSrc.visitTable, tsDst.visitTable};
        WqueueTableInterface[] wqueueTables = new WqueueTableInterface[]{tsSrc.wqueueTable, tsDst.wqueueTable};

        // statics
        copyDatabase(dbBackendSrc, byoumeiMasterTables[0], dbBackendDst, byoumeiMasterTables[1]);
        copyDatabase(dbBackendSrc, iyakuhinMasterTables[0], dbBackendDst, iyakuhinMasterTables[1]);
        copyDatabase(dbBackendSrc, kizaiMasterTables[0], dbBackendDst, kizaiMasterTables[1]);
        copyDatabase(dbBackendSrc, shinryouMasterTables[0], dbBackendDst, shinryouMasterTables[1]);
        copyDatabase(dbBackendSrc, shuushokugoMasterTables[0], dbBackendDst, shuushokugoMasterTables[1]);

        // semi-dynamics
        copyDatabase(dbBackendSrc, pharmaDrugTables[0], dbBackendDst, pharmaDrugTables[1]);
        copyDatabase(dbBackendSrc, prescExampleTables[0], dbBackendDst, prescExampleTables[1]);

        // dynamics
        copyDatabase(dbBackendSrc, patientTables[0], dbBackendDst, patientTables[1]);
        copyDatabase(dbBackendSrc, shahokokuhoTables[0], dbBackendDst, shahokokuhoTables[1]);
        copyDatabase(dbBackendSrc, koukikoureiTables[0], dbBackendDst, koukikoureiTables[1]);
        copyDatabase(dbBackendSrc, roujinTables[0], dbBackendDst, roujinTables[1]);
        copyDatabase(dbBackendSrc, kouhiTables[0], dbBackendDst, kouhiTables[1]);
        copyDatabase(dbBackendSrc, visitTables[0], dbBackendDst, visitTables[1]);
        copyDatabase(dbBackendSrc, textTables[0], dbBackendDst, textTables[1]);
        copyDatabase(dbBackendSrc, drugTables[0], dbBackendDst, drugTables[1]);
        copyDatabase(dbBackendSrc, drugAttrTables[0], dbBackendDst, drugAttrTables[1]);
        copyDatabase(dbBackendSrc, shinryouTables[0], dbBackendDst, shinryouTables[1]);
        copyDatabase(dbBackendSrc, shinryouAttrTables[0], dbBackendDst, shinryouAttrTables[1]);
        copyDatabase(dbBackendSrc, shoukiTables[0], dbBackendDst, shoukiTables[1]);
        copyDatabase(dbBackendSrc, wqueueTables[0], dbBackendDst, wqueueTables[1]);
        copyDatabase(dbBackendSrc, chargeTables[0], dbBackendDst, chargeTables[1]);
        copyDatabase(dbBackendSrc, conductTables[0], dbBackendDst, conductTables[1]);
        copyDatabase(dbBackendSrc, conductDrugTables[0], dbBackendDst, conductDrugTables[1]);
        copyDatabase(dbBackendSrc, conductKizaiTables[0], dbBackendDst, conductKizaiTables[1]);
        copyDatabase(dbBackendSrc, conductShinryouTables[0], dbBackendDst, conductShinryouTables[1]);
        copyDatabase(dbBackendSrc, gazouLabelTables[0], dbBackendDst, gazouLabelTables[1]);
        copyDatabase(dbBackendSrc, diseaseTables[0], dbBackendDst, diseaseTables[1]);
        copyDatabase(dbBackendSrc, diseaseAdjTables[0], dbBackendDst, diseaseAdjTables[1]);
        copyDatabase(dbBackendSrc, paymentTables[0], dbBackendDst, paymentTables[1]);
        copyDatabase(dbBackendSrc, pharmaQueueTables[0], dbBackendDst, pharmaQueueTables[1]);
        copyDatabase(dbBackendSrc, practiceLogTables[0], dbBackendDst, practiceLogTables[1]);
        copyDatabase(dbBackendSrc, hotlineTables[0], dbBackendDst, hotlineTables[1]);
        copyDatabase(dbBackendSrc, intraclinicPostTables[0], dbBackendDst, intraclinicPostTables[1]);
        copyDatabase(dbBackendSrc, intraclinicCommentTables[0], dbBackendDst, intraclinicCommentTables[1]);
        copyDatabase(dbBackendSrc, intraclinicTagTables[0], dbBackendDst, intraclinicTagTables[1]);
        copyDatabase(dbBackendSrc, intraclinicTagPostTables[0], dbBackendDst, intraclinicTagPostTables[1]);

    }

    private <DTO> void copyDatabase(DbBackend dbBackendSrc, TableInterface<DTO> tableInterfaceSrc,
                                    DbBackend dbBackendDst, TableInterface<DTO> tableInterfaceDst){

    }

    private DataSource getDataSource(String db) {
        switch (db) {
            case "mysql":
                return MysqlDataSource.create();
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

