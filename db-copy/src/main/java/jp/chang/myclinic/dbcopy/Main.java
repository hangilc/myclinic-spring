package jp.chang.myclinic.dbcopy;

import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableSet;
import jp.chang.myclinic.backenddb.tableinterface.*;
import jp.chang.myclinic.backendmysql.MysqlTableSet;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
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
            TableSet tsSrc = getTableSet(dbSrc, query);
            TableSet tsDst;
            ByoumeiMasterTableInterface[]  byoumeiMasterTables = new ByoumeiMasterTableInterface[]{ tsSrc.byoumeiMasterTable, tsDst.byoumeiMasterTable };
            ChargeTableInterface[]  chargeTables = new ChargeTableInterface[]{ tsSrc.chargeTable, tsDst.chargeTable };
            ConductTableInterface[]  conductTables = new ConductTableInterface[]{ tsSrc.conductTable, tsDst.conductTable };
            ConductDrugTableInterface[]  conductDrugTables = new ConductDrugTableInterface[]{ tsSrc.conductDrugTable, tsDst.conductDrugTable };
            ConductKizaiTableInterface[]  conductKizaiTables = new ConductKizaiTableInterface[]{ tsSrc.conductKizaiTable, tsDst.conductKizaiTable };
            ConductShinryouTableInterface[]  conductShinryouTables = new ConductShinryouTableInterface[]{ tsSrc.conductShinryouTable, tsDst.conductShinryouTable };
            DiseaseTableInterface[]  diseaseTables = new DiseaseTableInterface[]{ tsSrc.diseaseTable, tsDst.diseaseTable };
            DiseaseAdjTableInterface[]  diseaseAdjTables = new DiseaseAdjTableInterface[]{ tsSrc.diseaseAdjTable, tsDst.diseaseAdjTable };
            DrugTableInterface[]  drugTables = new DrugTableInterface[]{ tsSrc.drugTable, tsDst.drugTable };
            DrugAttrTableInterface[]  drugAttrTables = new DrugAttrTableInterface[]{ tsSrc.drugAttrTable, tsDst.drugAttrTable };
            GazouLabelTableInterface[]  gazouLabelTables = new GazouLabelTableInterface[]{ tsSrc.gazouLabelTable, tsDst.gazouLabelTable };
            HotlineTableInterface[]  hotlineTables = new HotlineTableInterface[]{ tsSrc.hotlineTable, tsDst.hotlineTable };
            IntraclinicCommentTableInterface[]  intraclinicCommentTables = new IntraclinicCommentTableInterface[]{ tsSrc.intraclinicCommentTable, tsDst.intraclinicCommentTable };
            IntraclinicPostTableInterface[]  intraclinicPostTables = new IntraclinicPostTableInterface[]{ tsSrc.intraclinicPostTable, tsDst.intraclinicPostTable };
            IntraclinicTagTableInterface[]  intraclinicTagTables = new IntraclinicTagTableInterface[]{ tsSrc.intraclinicTagTable, tsDst.intraclinicTagTable };
            IntraclinicTagPostTableInterface[]  intraclinicTagPostTables = new IntraclinicTagPostTableInterface[]{ tsSrc.intraclinicTagPostTable, tsDst.intraclinicTagPostTable };
            IyakuhinMasterTableInterface[]  iyakuhinMasterTables = new IyakuhinMasterTableInterface[]{ tsSrc.iyakuhinMasterTable, tsDst.iyakuhinMasterTable };
            KizaiMasterTableInterface[]  kizaiMasterTables = new KizaiMasterTableInterface[]{ tsSrc.kizaiMasterTable, tsDst.kizaiMasterTable };
            KouhiTableInterface[]  kouhiTables = new KouhiTableInterface[]{ tsSrc.kouhiTable, tsDst.kouhiTable };
            KoukikoureiTableInterface[]  koukikoureiTables = new KoukikoureiTableInterface[]{ tsSrc.koukikoureiTable, tsDst.koukikoureiTable };
            PatientTableInterface[]  patientTables = new PatientTableInterface[]{ tsSrc.patientTable, tsDst.patientTable };
            PaymentTableInterface[]  paymentTables = new PaymentTableInterface[]{ tsSrc.paymentTable, tsDst.paymentTable };
            PharmaDrugTableInterface[]  pharmaDrugTables = new PharmaDrugTableInterface[]{ tsSrc.pharmaDrugTable, tsDst.pharmaDrugTable };
            PharmaQueueTableInterface[]  pharmaQueueTables = new PharmaQueueTableInterface[]{ tsSrc.pharmaQueueTable, tsDst.pharmaQueueTable };
            PracticeLogTableInterface[]  practiceLogTables = new PracticeLogTableInterface[]{ tsSrc.practiceLogTable, tsDst.practiceLogTable };
            PrescExampleTableInterface[]  prescExampleTables = new PrescExampleTableInterface[]{ tsSrc.prescExampleTable, tsDst.prescExampleTable };
            RoujinTableInterface[]  roujinTables = new RoujinTableInterface[]{ tsSrc.roujinTable, tsDst.roujinTable };
            ShahokokuhoTableInterface[]  shahokokuhoTables = new ShahokokuhoTableInterface[]{ tsSrc.shahokokuhoTable, tsDst.shahokokuhoTable };
            ShinryouTableInterface[]  shinryouTables = new ShinryouTableInterface[]{ tsSrc.shinryouTable, tsDst.shinryouTable };
            ShinryouAttrTableInterface[]  shinryouAttrTables = new ShinryouAttrTableInterface[]{ tsSrc.shinryouAttrTable, tsDst.shinryouAttrTable };
            ShinryouMasterTableInterface[]  shinryouMasterTables = new ShinryouMasterTableInterface[]{ tsSrc.shinryouMasterTable, tsDst.shinryouMasterTable };
            ShoukiTableInterface[]  shoukiTables = new ShoukiTableInterface[]{ tsSrc.shoukiTable, tsDst.shoukiTable };
            ShuushokugoMasterTableInterface[]  shuushokugoMasterTables = new ShuushokugoMasterTableInterface[]{ tsSrc.shuushokugoMasterTable, tsDst.shuushokugoMasterTable };
            TextTableInterface[]  textTables = new TextTableInterface[]{ tsSrc.textTable, tsDst.textTable };
            VisitTableInterface[]  visitTables = new VisitTableInterface[]{ tsSrc.visitTable, tsDst.visitTable };
            WqueueTableInterface[]  wqueueTables = new WqueueTableInterface[]{ tsSrc.wqueueTable, tsDst.wqueueTable };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TableSet getTableSet(String db, Query query){
        switch(db){
            case "mysql": {
                return MysqlTableSet.create(query);
            }
            case "sqlite": {
                return SqliteTableSet.create(query);
            }
            default:
                throw new RuntimeException("Cannot find database: " + db);
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

