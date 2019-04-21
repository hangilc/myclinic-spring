package jp.chang.myclinic.backendmysql;

import com.mysql.cj.jdbc.MysqlDataSource;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.SupportSet;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.kizaicodes.KizaicodeFileResolver;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryoucodes.ShinryoucodeFileResolver;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Paths;

public class Test {

    public static void main(String[] args){
        DataSource ds = MysqlDataSourceProvider.create();
        DbBackend dbBackend = new DbBackend(ds, MysqlTableSet::create, createSupportSet());
        confirmTestDatabase(dbBackend);
        new Tester().test(dbBackend);
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

    private static void confirmTestDatabase(DbBackend dbBackend){
        boolean ok = dbBackend.query(backend -> {
            PatientDTO patient = backend.getPatient(1);
            return patient != null && patient.lastName.equals("試験") &&
                    patient.firstName.equals("データ");
        });
        if( !ok ){
            System.err.println("Database is not for testing purpose.");
            System.exit(1);
        }
    }

}
