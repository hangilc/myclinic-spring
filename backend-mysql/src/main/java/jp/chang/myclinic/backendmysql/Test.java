package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.backenddb.DBImpl;
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
import picocli.CommandLine;
import picocli.CommandLine.*;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jp.chang.myclinic.backendmysql.MysqlDataSourceProvider.MysqlDataSourceConfig;

@Command(name = "test-mysql")
public class Test implements Runnable {

    @Option(names = "--host", description = "MySQL database host")
    private String dbHost = "192.169.33.10";

    @Option(names = "--thread", description = "Number of concurrent threads for testing")
    private int threadSize = 1;

    public static void main(String[] args){
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

    @Override
    public void run() {
        MysqlDataSourceConfig config = new MysqlDataSourceConfig()
                .host(dbHost);
        DataSource ds = MysqlDataSourceProvider.create(config);
        DB db = new DBImpl(ds);
        DbBackend dbBackend = new DbBackend(db, MysqlTableSet::create, createSupportSet());
        confirmTestDatabase(dbBackend);
        if( threadSize == 1 ) {
            new Tester().test(dbBackend);
        } else {
            List<Future<Void>> futures = new ArrayList<>();
            for(int i=0;i<threadSize;i++){
                ExecutorService service = Executors.newSingleThreadExecutor(r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                });
                Future<Void> future = service.submit(() -> {
                    new Tester().test(dbBackend);
                    return null;
                });
                futures.add(future);
            }
            try {
                for (Future future : futures) {
                    future.get();
                }
                System.out.println("Test completed successfully.");
            } catch(Exception ex){
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
}
