package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.SupportSet;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.kizaicodes.KizaicodeFileResolver;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryoucodes.ShinryoucodeFileResolver;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;
import picocli.CommandLine;
import picocli.CommandLine.*;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Paths;

@Command(name = "test", mixinStandardHelpOptions = true)
public class Test implements Runnable {

    @Parameters(paramLabel = "SQLite db file", arity = "0..1", description = "SQLite db file to use for testing")
    private String dbFile = Paths.get(System.getProperty("user.home"), "sqlite-data",
            "myclinic-test-sqlite.db").toString();

    public static void main(String[] args) {
        CommandLine.run(new Test(), args);
    }

    private static SupportSet createSupportSet(){
        SupportSet ss = new SupportSet();
        ss.stockDrugService = new StockDrugFile(Paths.get("config/stock-drug.txt"));
        ss.houkatsuKensaService = new HoukatsuKensaFile(Paths.get("config/houkatsu-kensa.xml"));
        ss.meisaiService = new MeisaiServiceImpl();
        ss.diseaseExampleProvider = new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml"));
        ss.shinryoucodeResolver = new ShinryoucodeFileResolver(new File("config/shinryoucodes.yml"));
        ss.kizaicodeResolver = new KizaicodeFileResolver(new File("config/kizaicodes.yml"));
        ss.clinicInfoProvider = new ClinicInfoFileProvider(Paths.get("config/clinic-info.yml"));
        return ss;
    }

    @Override
    public void run() {
        DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
        DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create, createSupportSet());
        new Tester().test(dbBackend);
    }
}

