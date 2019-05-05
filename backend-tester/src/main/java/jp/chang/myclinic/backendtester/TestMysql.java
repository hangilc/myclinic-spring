package jp.chang.myclinic.backendtester;

import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.backenddb.DBImpl;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.backendmysql.MysqlDataSourceProvider;
import jp.chang.myclinic.backendmysql.MysqlDataSourceProvider.MysqlDataSourceConfig;
import jp.chang.myclinic.backendmysql.MysqlTableSet;
import jp.chang.myclinic.dto.PatientDTO;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Command(name = "mysql")
public class TestMysql implements Runnable {

    @Option(names = "--host", description = "MySQL database host")
    private String dbHost = "192.168.33.10";

    @Option(names = "--thread", description = "Number of concurrent threads for testing")
    private int threadSize = 1;

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
        DbBackend dbBackend = new DbBackend(db, MysqlTableSet::create);
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
