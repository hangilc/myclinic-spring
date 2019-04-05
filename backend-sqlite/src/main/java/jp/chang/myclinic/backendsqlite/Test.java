package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.SupportSet;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaService;
import jp.chang.myclinic.support.kizainames.KizaiNamesFile;
import jp.chang.myclinic.support.kizainames.KizaiNamesService;
import jp.chang.myclinic.support.meisai.MeisaiService;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesFile;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesService;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;
import jp.chang.myclinic.support.stockdrug.StockDrugService;

import javax.sql.DataSource;
import java.nio.file.Paths;

public class Test {

    public static void main(String[] args) {
        if( args.length != 1 ){
            System.err.println("Usage: Test dbFile");
            System.exit(1);
        }
        String dbFile = args[0];
        DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
        DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create, createSupportSet());
        new Tester().test(dbBackend);
    }

    private static SupportSet createSupportSet(){
        SupportSet ss = new SupportSet();
        ss.stockDrugService = new StockDrugFile(Paths.get("config/stock-drug.txt"));
        ss.houkatsuKensaService = new HoukatsuKensaFile(Paths.get("conifg/houkatsu-kensa.xml"));
        ss.meisaiService = new MeisaiServiceImpl();
        ss.diseaseExampleProvider = new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml"));
        ss.shinryouNamesService = new ShinryouNamesFile(Paths.get("config/shinryou-names.yml"));
        ss.kizaiNamesService = new KizaiNamesFile(Paths.get("config/kizai-names.yml"));
        ss.clinicInfoProvider = new ClinicInfoFileProvider(Paths.get("config/clinic-info.yml"));
        return ss;
    }

}

